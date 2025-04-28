import subprocess
import os
import re

# 📁 설정
local_dir = r"C:/Users/USER/PycharmProjects/hackathon2025"
file_name = input("파일 이름 입력: ")
file_path = os.path.join(local_dir, file_name)

# 📍 디렉토리 이동
os.chdir(local_dir)

# 🔄 브랜치 확인 및 전환
branch_result = subprocess.run(["git", "rev-parse", "--abbrev-ref", "HEAD"],
                               capture_output=True, text=True)
current_branch = branch_result.stdout.strip()

if current_branch != "LJM":
    print(f"❌ 현재 브랜치: {current_branch}")
    print("🔁 LJM 브랜치로 전환합니다.")
    subprocess.run(["git", "checkout", "LJM"], capture_output=True, text=True)
else:
    print("✅ 현재 브랜치: LJM")

# 🔄 최신화
subprocess.run(["git", "pull", "origin", "LJM"], capture_output=True, text=True)
print("✅ LJM 브랜치 최신화 완료!")

# 🧮 등급 → 점수 변환 함수
def grade_to_scores(grade):
    scores = {
        'A': [100, 95, 90, 95, 100],
        'B': [85, 80, 75, 85, 90],
        'C': [70, 65, 60, 70, 75],
        'D': [50, 40, 40, 50, 55],
        'E': [30, 25, 20, 30, 35],
        'F': [10, 10, 10, 10, 10],
    }
    return scores.get(grade.upper(), [0, 0, 0, 0, 0])
# ✅ 점수 → 등급 문자 역변환 함수
def score_to_grade(score):
    if score >= 90:
        return "A"
    elif score >= 70:
        return "B"
    elif score >= 50:
        return "C"
    elif score >= 30:
        return "D"
    elif score >= 10:
        return "E"
    else:
        return "F"

# 📊 점수 분석 함수 (정수화 및 등급 매핑 포함)
def analyze_scores(radon_output):
    grades = re.findall(r'\b[A-F]\b', radon_output)
    if not grades:
        print("❗ 등급 정보를 찾을 수 없습니다.")
        return [0, 0, 0, 0, 0]

    total_scores = [0] * 5
    for grade in grades:
        grade_scores = grade_to_scores(grade)
        total_scores = [sum(x) for x in zip(total_scores, grade_scores)]

    avg_scores = [round(s / len(grades)) for s in total_scores]  # 정수 점수
    return avg_scores

# 📦 radon 분석 실행
def analyze_radon(file_path):
    try:
        cmd = ["radon", "cc", file_path, "-a", "-s"]
        result = subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8", shell=True)
        output = result.stdout

        print("📊 radon 코드 복잡도 분석 결과:")
        print(output)

        # 점수 분석
        scores = analyze_scores(output)

        # 점수 변수 저장
        structure_score = scores[0]
        testability_score = scores[1]
        maintainability_score = scores[2]
        readability_score = scores[3]
        quality_score = scores[4]

        # 등급 매핑
        structure_grade = score_to_grade(structure_score)
        testability_grade = score_to_grade(testability_score)
        maintainability_grade = score_to_grade(maintainability_score)
        readability_grade = score_to_grade(readability_score)
        quality_grade = score_to_grade(quality_score)

        # 출력
        print("\n📈 항목별 평가 결과:")
        print(f"• 구조 복잡도: {structure_score}점 ({structure_grade} 등급)")
        print(f"• 테스트 용이성: {testability_score}점 ({testability_grade} 등급)")
        print(f"• 유지 보수성: {maintainability_score}점 ({maintainability_grade} 등급)")
        print(f"• 가독성: {readability_score}점 ({readability_grade} 등급)")
        print(f"• 코드 품질: {quality_score}점 ({quality_grade} 등급)")

        return {
            "structure": structure_score,
            "testability": testability_score,
            "maintainability": maintainability_score,
            "readability": readability_score,
            "quality": quality_score
        }

    except Exception as e:
        print(f"❌ 오류 발생: {e}")
        return None


# 🚀 실행
print(f"\n📂 분석 대상 파일: {file_path}")
score_dict = analyze_radon(file_path)





# 구조 복잡도: radon의 cyclomatic complexity 그대로.

# 테스트 용이성: 복잡도 낮을수록 유닛 테스트 작성이 쉽다는 가정.

# 유지 보수성: 함수의 복잡도와 크기(라인 수)가 작을수록 유리.

# 가독성: 복잡도가 낮으면 코드 흐름 파악이 쉬워서 높게 평가.

# 코드 품질: 위 요소들을 종합한 평균 점수로 판단.