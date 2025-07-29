import React from 'react'
import styled from 'styled-components'

function CodeAnalysis() {
  return (
    <Container>
      <Title>홈페이지에 오신 걸 환영합니다 👋</Title>
      <Description>이 사이트는 Vite + React + Styled Components 기반입니다.</Description>
      <StartButton onClick={() => alert('시작합니다! 🚀')}>
        시작하기
      </StartButton>
    </Container>
  )
}

export default CodeAnalysis;

