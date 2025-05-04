package com.example.hackathonback.problem.entity;

/**
 * 문제의 주제를 분류하는 태그 Enum
 * - GPT 추천, 문제 생성, 검색 등에 사용
 */
public enum ProblemTag {

    // 기초 문법
    PRINT,              // 출력
    VARIABLE,           // 변수
    DATATYPE,           // 자료형

    // 자료구조
    ARRAY,              // 배열
    STRING,             // 문자열
    STACK,              // 스택
    QUEUE,              // 큐
    HASH,               // 해시
    SET,                // 집합
    MAP,                // 맵
    TREE,               // 트리
    HEAP,               // 힙
    PRIORITY_QUEUE,     // 우선순위 큐

    // 제어문
    CONDITIONAL,        // 조건문
    LOOP,               // 반복문
    FUNCTION,           // 함수

    // 알고리즘 기초
    SORTING,            // 정렬
    SEARCH,             // 탐색
    BRUTE_FORCE,        // 완전 탐색
    BACKTRACKING,       // 백트래킹
    DFS,                // 깊이 우선 탐색
    BFS,                // 너비 우선 탐색
    GREEDY,             // 그리디 알고리즘
    DP,                 // 동적 계획법
    BINARY_SEARCH,      // 이분 탐색

    // 알고리즘 심화
    GRAPH,              // 그래프
    SHORTEST_PATH,      // 최단 경로
    TOPOLOGY,           // 위상 정렬
    BIT_OPERATION,      // 비트 연산
    BIT_MASKING,        // 비트 마스킹
    REGEX,              // 정규 표현식
    SIMULATION,         // 시뮬레이션
    MATH,               // 수학
    RECURSION           // 재귀
}
