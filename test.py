import time
import random


# 카드 클래스를 정의합니다.
class card:
    def __init__(self, HP, MIN_ATK, MAX_ATK, STUN):
        # 카드의 HP, 최소/최대 공격력, 스턴 상태를 초기화합니다.
        self.hp = HP
        self.min_atk = MIN_ATK
        self.max_atk = MAX_ATK
        self.stun = STUN
        self.p1_item_num = [3, 3, 1]  # 플레이어 1의 아이템 수 (체력 회복, 공격력 증가, 스턴 제거)
        self.p2_item_num = [3, 3, 1]  # 플레이어 2의 아이템 수 (체력 회복, 공격력 증가, 스턴 제거)

    def attack(self, target):
        # 공격을 실행하고 랜덤 공격력을 결정한 후 상대의 HP를 감소시킵니다.
        ran_dam = random.randrange(self.min_atk, self.max_atk)
        target.hp -= ran_dam
        if target.hp <= 0:
            print("Enemy card is Break!")
        else:
            # 공격 후, 공격된 카드의 HP를 출력합니다.
            print("Damage: ", ran_dam)
            print('*************')
            print("Enemy HP: ", target.hp)
            print('*************')

    def use_item(self, item_number, target, name):
        # 아이템 사용을 처리합니다.
        if name == "p1":
            # 플레이어 1의 아이템을 사용합니다.
            if item_number == '1' and self.p1_item_num[0] > 0:
                # 아이템 1: 체력 10 회복
                target.hp += 10
                self.p1_item_num[0] -= 1
                print("Your HP:", target.hp, "\n*************")
            elif item_number == '2' and self.p1_item_num[1] > 0:
                # 아이템 2: 공격력 +5
                self.min_atk += 5
                self.max_atk += 5
                self.p1_item_num[1] -= 1
                print("Your Min, Max ATK:", self.min_atk, ",", self.max_atk, "\n*************")
            elif item_number == '3' and self.p1_item_num[2] > 0:
                # 아이템 3: 스턴 제거
                self.stun = 0
                self.p1_item_num[2] -= 1
            elif item_number == '4':
                # 아이템 4: 상대방 공격 (상대의 공격력만 초기화)
                target.min_atk = target.min_atk
            else:
                print("아이템이 없거나 다른것을 입력하셨습니다.\n실수 여부 상관없이 상대 차례가 됩니다.")
                print('*************')
                time.sleep(1)
        else:
            # 플레이어 2의 아이템을 사용합니다.
            if item_number == '1' and self.p2_item_num[0] > 0:
                target.hp += 10
                self.p2_item_num[0] -= 1
                print("Your HP:", target.hp, "\n*************")
            elif item_number == '2' and self.p2_item_num[1] > 0:
                self.min_atk += 5
                self.max_atk += 5
                self.p2_item_num[1] -= 1
                print("Your Min, Max ATK:", self.min_atk, ",", self.max_atk, "\n*************")
            elif item_number == '3' and self.p2_item_num[2] > 0:
                self.stun = 0
                self.p2_item_num[2] -= 1
            elif item_number == '4':
                target.min_atk = target.min_atk
            else:
                print("아이템이 없거나 다른것을 입력하셨습니다.\n실수 여부 상관없이 상대 차례가 됩니다.")
                print('*************')
                time.sleep(1)
        return item_number

    def check_item(self, name):
        # 아이템의 갯수를 출력하고 반환합니다.
        if name == "p1":
            self.p1_item_n = self.p1_item_num[0] + self.p1_item_num[1] + self.p1_item_num[2]
            print('아이템 갯수:', self.p1_item_num[0], self.p1_item_num[1], self.p1_item_num[2])
            return self.p1_item_n
        else:
            self.p2_item_n = self.p2_item_num[0] + self.p2_item_num[1] + self.p2_item_num[2]
            print('아이템 갯수:', self.p2_item_num[0], self.p2_item_num[1], self.p2_item_num[2])
            return self.p2_item_n

    def stuned(self, name):
        # 스턴 상태를 처리합니다. 일정 확률로 상대방이 스턴에 걸리게 됩니다.
        ran = random.randint(0, 100)
        ran //= 10
        time.sleep(0.5)
        if ran == random.randint(0, 10):
            print("Enemy Stun +1")
            # 스턴 해제 아이템을 사용하는지 묻습니다.
            if self.p2_item_num[2] > 0:
                anwser = input("3번 아이템(본인에게 있는 스턴 제거) 를 쓰시겠습니까? (Y/N): ")
                if anwser == 'Y':
                    print("스턴이 풀렸습니다!")
                    if name == 'p1':
                        self.stun = 0
                        self.p1_item_num[2] -= 1
                    else:
                        self.stun = 0
                        self.p2_item_num[2] -= 1
                else:
                    self.stun += 1
            else:
                self.stun += 1
            print('*************')
            return 1

    def stun_down(self):
        # 스턴을 1 감소시킵니다.
        self.stun -= 1

    def stun_check(self):
        # 스턴 상태를 확인합니다.
        return self.stun


# 플레이어 1과 2의 카드 객체를 생성합니다.
p1 = card(int(input("Player 1's HP: ")), int(input("Player 1's MIN ATK: ")), int(input("Player 1's MAX ATK: ")), 0)
p2 = card(int(input("Player 2's HP: ")), int(input("Player 2's MIN ATK: ")), int(input("Player 2's MAX ATK: ")), 0)

tem_use = ""
print("아이템은 각 각 3개씩 주어집니다. 하지만 스턴 제거 아이템 (3번 아이템)은 1개씩만 주어집니다")
print('*************')

# 게임 루프 (무한 반복)
for i in range(1, 99999999):
    print("Player 1's Turn!")
    if p1.stun_check() == 0:
        if not p1.check_item("p1") == 0:
            # 플레이어 1의 아이템을 사용합니다.
            tem_use = p1.use_item(input("사용할 아이템을 선택해 주십시오.\n1: 체력 10 회복, 2: 최소, 최대 공격력 +5, 3: 본인에게 있는 스턴 제거, 4: 상대방 공격: "), p1, "p1")
        if tem_use == "4":
            # 아이템 4로 상대방 공격
            time.sleep(1)
            print('*************')
            print("Player 1's Attack!")
            p1.attack(p2)
            if p1.hp <= 0 or p2.hp <= 0:
                break
            p2.stuned('p2')
    else:
        # 스턴 상태일 경우
        print("You Stunned!")
        print('*************')
        p1.stun_down()
    time.sleep(1.5)

    print("Player 2's Turn!")
    if p2.stun_check() == 0:
        if not p2.check_item("p2") == 0:
            # 플레이어 2의 아이템을 사용합니다.
            tem_use = p2.use_item(input("사용할 아이템을 선택해 주십시오.\n1: 체력 10 회복, 2: 최소, 최대 공격력 +5, 3: 본인에게 있는 스턴 제거, 4: 상대방 공격: "), p2, "p2")
        if tem_use == "4":
            # 아이템 4로 상대방 공격
            time.sleep(1)
            print('*************')
            print("Player 2's Attack!")
            p2.attack(p1)
            if p1.hp <= 0 or p2.hp <= 0:
                break
            p1.stuned('p1')
    else:
        # 스턴 상태일 경우
        print("You Stunned!")
        print('*************')
        p2.stun_down()
    time.sleep(1.5)

# 게임 종료 후 승자 출력
if p1.hp <= 0:
    print("\nPlayer 2 is Win!")
else:
    print("\nPlayer 1 is Win!")