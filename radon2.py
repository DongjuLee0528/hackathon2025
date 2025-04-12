import subprocess
import os

# 로컬 저장소와 브랜치를 확인하고, 해당 브랜치에서 작업할 수 있도록 설정
local_dir = "C:/Users/USER/PycharmProjects/hackathon2025"  # 로컬 저장소 디렉토리
file_name = "test.py"  # 분석할 파일 이름

# 로컬 디렉토리로 이동
os.chdir(local_dir)

# 현재 브랜치가 LJM인지 확인 후 이동
result = subprocess.run(["git", "rev-parse", "--abbrev-ref", "HEAD"], capture_output=True, text=True)
current_branch = result.stdout.strip()

if current_branch == "LJM":
    print("✅ LJM 브랜치에 있습니다!")
else:
    print("❌ LJM 브랜치에 없습니다. LJM 브랜치로 전환합니다.")
    subprocess.run(["git", "checkout", "LJM"], capture_output=True, text=True)

# git pull로 최신화
subprocess.run(["git", "pull", "origin", "LJM"], capture_output=True, text=True)
print("✅ LJM 브랜치 최신화 완료!")

# radon cc를 사용해 파일 분석
def analyze_radon(file_path):
    """
    주어진 파일을 radon으로 분석하고 복잡도를 출력합니다.
    """
    try:
        cmd = ["radon", "cc", file_path, "-a", "-s"]
        result = subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8")
        print("📊 radon 코드 복잡도 분석 결과:")
        print(result.stdout)

    except Exception as e:
        print(f"❌ 오류 발생: {e}")

# 분석할 파일 경로 설정
file_path = os.path.join(local_dir, file_name)

# 파일 분석 실행
analyze_radon(file_path)
