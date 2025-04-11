import subprocess

def analyze_radon(file_path):
    cmd = ["radon", "cc", file_path, "-a", "-s"]
    result = subprocess.run(cmd, capture_output=True, text=True)
    print(result.stdout)

analyze_radon("test.py")
