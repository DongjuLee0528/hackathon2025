import React from 'react'
import styled from 'styled-components'


// 👇 Styled-Components 아래쪽에 같이 정의함
const Container = styled.div`
  // width: 100%;
  min-height: 100vh;
  background: linear-gradient(135deg, #f9f9f9, #dde5ff);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 2rem;
`

const Title = styled.h1`
  font-size: 3rem;
  color: #2c3e50;
  margin-bottom: 1rem;
`

const Description = styled.p`
  font-size: 1.25rem;
  color: #555;
  margin-bottom: 2rem;
  text-align: center;
  max-width: 600px;
`

const StartButton = styled.button`
  background-color: #4a5cff;
  color: white;
  padding: 0.8rem 1.6rem;
  font-size: 1rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;

  &:hover {
    background-color: #3a4ce0;
  }
`
  const apipage = styled.button`
    background-color: #4a5cff;
  color: white;
  padding: 0.8rem 1.6rem;
  font-size: 1rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  `

function Home() {
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

export default Home

