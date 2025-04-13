# 3x3 보드 초기화
board = [[' ' for _ in range(3)] for _ in range(3)]

def print_board():
    print("\n  0   1   2")
    for idx, row in enumerate(board):
        print(f"{idx} " + " | ".join(row))
        if idx < 2:
            print("  ---------")

def is_valid_move(x, y):
    return 0 <= x < 3 and 0 <= y < 3 and board[y][x] == ' '

def place_mark(x, y, player):
    board[y][x] = 'X' if player == 1 else 'O'

def check_win(player):
    mark = 'X' if player == 1 else 'O'
    # 가로, 세로
    for i in range(3):
        if all(board[i][j] == mark for j in range(3)):  # 가로
            return True
        if all(board[j][i] == mark for j in range(3)):  # 세로
            return True
    # 대각선
    if all(board[i][i] == mark for i in range(3)):
        return True
    if all(board[i][2 - i] == mark for i in range(3)):
        return True
    return False

def is_draw():
    return all(cell != ' ' for row in board for cell in row)

def game_loop():
    player = 1
    print_board()
    while True:
        print(f"\nPlayer {player} ({'X' if player == 1 else 'O'}) 차례입니다.")
        try:
            move = input("좌표 입력 (x y): ").split()
            if len(move) != 2:
                print("⚠️ 좌표를 x y 형식으로 입력해주세요.")
                continue
            x, y = map(int, move)
            if not is_valid_move(x, y):
                print("❌ 잘못된 위치입니다. 다시 입력해주세요.")
                continue
            place_mark(x, y, player)
            print_board()
            if check_win(player):
                print(f"\n🎉 Player {player} ({'X' if player == 1 else 'O'}) 승리!")
                break
            if is_draw():
                print("\n🤝 무승부입니다!")
                break
            player = 3 - player  # 1 ↔ 2 전환
        except ValueError:
            print("⚠️ 숫자를 입력해주세요.")
        except KeyboardInterrupt:
            print("\n게임을 종료합니다.")
            break

if __name__ == "__main__":
    print("🎮 틱택토 게임 시작! (3x3)")
    game_loop()
