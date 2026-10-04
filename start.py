import os
import sys
import time
import subprocess
import shutil

def find_sdk_tool(tool_name):
    """Locate Android SDK tool (adb, emulator) from environment or standard paths."""
    # Check if tool is directly in PATH
    in_path = shutil.which(tool_name)
    if in_path:
        return in_path

    # Check ANDROID_HOME or LOCALAPPDATA
    sdk_root = os.environ.get("ANDROID_HOME") or os.path.join(os.environ.get("LOCALAPPDATA", ""), "Android", "Sdk")
    
    subdirs = {
        "adb": ["platform-tools"],
        "adb.exe": ["platform-tools"],
        "emulator": ["emulator"],
        "emulator.exe": ["emulator"],
    }
    
    for subdir in subdirs.get(tool_name.lower(), []):
        candidate = os.path.join(sdk_root, subdir, tool_name)
        if os.path.isfile(candidate):
            return candidate
            
    return tool_name

def run_cmd(cmd, check=True, capture=True):
    """Run a command and return stdout/stderr."""
    if isinstance(cmd, list):
        print(f"-> {' '.join(cmd)}")
    else:
        print(f"-> {cmd}")
    result = subprocess.run(cmd, shell=isinstance(cmd, str), capture_output=capture, text=True)
    if check and result.returncode != 0:
        print(f"Command failed with code {result.returncode}:\n{result.stderr}")
        sys.exit(result.returncode)
    return result

def get_running_devices(adb_bin):
    """Check currently connected adb devices."""
    res = run_cmd([adb_bin, "devices"], check=False)
    lines = res.stdout.strip().splitlines()[1:]
    devices = [line.split()[0] for line in lines if line.strip() and "offline" not in line and "device" in line]
    return devices

def get_available_avds(emulator_bin):
    """List available AVD names."""
    res = run_cmd([emulator_bin, "-list-avds"], check=False)
    avds = [line.strip() for line in res.stdout.strip().splitlines() if line.strip()]
    return avds

def wait_for_boot(adb_bin, timeout=120):
    """Wait until device is fully booted and responsive."""
    print("Waiting for device to connect to adb...")
    run_cmd([adb_bin, "wait-for-device"])
    
    print("Waiting for Android OS to finish booting (sys.boot_completed=1)...")
    start_time = time.time()
    while time.time() - start_time < timeout:
        res = subprocess.run([adb_bin, "shell", "getprop", "sys.boot_completed"], capture_output=True, text=True)
        if res.stdout.strip() == "1":
            print("Android OS booted successfully! [OK]")
            return True
        time.sleep(2)
        print(".", end="", flush=True)
    print("\nWarning: Boot check timed out. Proceeding anyway...")
    return False

def build_apk_if_needed(project_dir):
    """Build debug APK using gradlew if missing or outdated."""
    apk_path = os.path.join(project_dir, "app", "build", "outputs", "apk", "debug", "app-debug.apk")
    gradlew = os.path.join(project_dir, "gradlew.bat" if sys.platform == "win32" else "gradlew")
    
    if not os.path.isfile(apk_path):
        print("\nDebug APK not found. Building with Gradle...")
        run_cmd(f'"{gradlew}" assembleDebug', check=True)
    else:
        print(f"Found debug APK: {apk_path}")
        
    return apk_path

def main():
    project_dir = os.path.abspath(os.path.dirname(__file__))
    os.chdir(project_dir)
    
    exe_ext = ".exe" if sys.platform == "win32" else ""
    adb_bin = find_sdk_tool(f"adb{exe_ext}")
    emulator_bin = find_sdk_tool(f"emulator{exe_ext}")
    
    print("=" * 60)
    print(" Splitz: Android Emulator & App Launcher")
    print("=" * 60)
    print(f"Project directory: {project_dir}")
    print(f"Using ADB:         {adb_bin}")
    print(f"Using Emulator:    {emulator_bin}")
    
    # 1. Check if emulator/device is already running
    running = get_running_devices(adb_bin)
    if running:
        print(f"\nFound active running device(s): {', '.join(running)}")
    else:
        print("\nNo running device found. Starting an emulator...")
        avds = get_available_avds(emulator_bin)
        if not avds:
            print("ERROR: No Android Virtual Devices (AVD) found!")
            print("Please create an emulator in Android Studio (Tools -> Device Manager).")
            sys.exit(1)
            
        selected_avd = avds[0]
        print(f"Starting AVD: '{selected_avd}'...")
        
        # Start emulator in detached background process
        emulator_cmd = [emulator_bin, "-avd", selected_avd, "-netdelay", "none", "-netspeed", "full"]
        if sys.platform == "win32":
            subprocess.Popen(emulator_cmd, creationflags=subprocess.DETACHED_PROCESS | subprocess.CREATE_NEW_PROCESS_GROUP)
        else:
            subprocess.Popen(emulator_cmd, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
            
        wait_for_boot(adb_bin)

    # 2. Build APK if needed
    apk_path = build_apk_if_needed(project_dir)

    # 3. Install APK
    print(f"\nInstalling {os.path.basename(apk_path)} on device...")
    install_res = run_cmd([adb_bin, "install", "-r", "-d", apk_path], check=False)
    if "Success" in install_res.stdout:
        print("Installation successful! [OK]")
    else:
        print(f"Install output:\n{install_res.stdout}\n{install_res.stderr}")

    # 4. Launch Main Activity
    package_name = "com.paisede.app"
    activity_name = ".MainActivity"
    component = f"{package_name}/{activity_name}"
    print(f"\nLaunching {component}...")
    run_cmd([adb_bin, "shell", "am", "start", "-n", component, "-a", "android.intent.action.MAIN", "-c", "android.intent.category.LAUNCHER"])
    
    print("\n" + "=" * 60)
    print(" Splitz is now running on the Android emulator!")
    print("=" * 60)

if __name__ == "__main__":
    main()
