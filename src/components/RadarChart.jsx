import React from 'react';
import styled from 'styled-components';
import {
  Chart as ChartJS,
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend
} from 'chart.js';
import { Radar } from 'react-chartjs-2';

ChartJS.register(
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend
);

// Styled Components
const ChartContainer = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
`;

const ChartTitle = styled.h3`
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 24px;
  font-weight: 700;
  color: #2e7d32;
  margin: 0 0 20px 0;
  text-align: center;
`;

const ChartWrapper = styled.div`
  width: 100%;
  max-width: 500px;
  height: 400px;
  position: relative;
  margin-bottom: 20px;
`;

const ScoresSummary = styled.div`
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 100%;
  max-width: 500px;
  margin-top: 20px;
`;

const ScoreItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border-left: 4px solid #4caf50;
  font-family: 'Noto Sans KR', sans-serif;
`;

const ScoreLabel = styled.span`
  font-size: 16px;
  font-weight: 500;
  color: #333;
`;

const ScoreValue = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`;

const ScoreNumber = styled.span`
  font-size: 18px;
  font-weight: 700;
  color: #2e7d32;
`;

const ScoreGrade = styled.span`
  font-size: 14px;
  padding: 4px 8px;
  border-radius: 12px;
  font-weight: 600;
  color: #fff;
  background-color: ${props => {
    const score = props.score;
    if (score >= 90) return '#4caf50';
    if (score >= 80) return '#8bc34a';
    if (score >= 70) return '#ffc107';
    if (score >= 60) return '#ff9800';
    return '#f44336';
  }};
`;

const AverageScore = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: linear-gradient(135deg, #4caf50, #2e7d32);
  border-radius: 12px;
  color: white;
  font-family: 'Noto Sans KR', sans-serif;
  margin-top: 10px;
`;

const AverageLabel = styled.span`
  font-size: 18px;
  font-weight: 600;
`;

const AverageValue = styled.span`
  font-size: 24px;
  font-weight: 700;
`;

const getGradeFromScore = (score) => {
  if (score >= 90) return '탁월';
  if (score >= 80) return '우수';
  if (score >= 70) return '양호';
  if (score >= 60) return '보통';
  return '개선 필요';
};

const RadarChart = ({ ratings = [80, 90, 70, 85, 75], title = "코드 품질 분석 결과" }) => {
  const labels = ['구조적 복잡도', '코드 품질', '가독성', '유지보수성', '테스트 용이성'];
  
  const data = {
    labels,
    datasets: [
      {
        label: '절대 평가 점수',
        data: ratings,
        backgroundColor: 'rgba(76, 175, 80, 0.2)',
        borderColor: 'rgb(46, 125, 50)',
        pointBackgroundColor: 'rgb(46, 125, 50)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgb(46, 125, 50)',
        pointRadius: 6,
        pointHoverRadius: 8,
        borderWidth: 3
      }
    ]
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      r: {
        beginAtZero: true,
        max: 100,
        min: 0,
        ticks: {
          stepSize: 20,
          font: {
            size: 12,
            family: "'Noto Sans KR', sans-serif"
          },
          color: '#666',
          backdropColor: 'transparent'
        },
        pointLabels: {
          font: {
            size: 14,
            family: "'Noto Sans KR', sans-serif",
            weight: '600'
          },
          color: '#333'
        },
        grid: {
          color: 'rgba(0,0,0,0.1)',
          lineWidth: 1
        },
        angleLines: {
          color: 'rgba(0,0,0,0.1)',
          lineWidth: 1
        }
      }
    },
    plugins: {
      legend: {
        display: false
      },
      tooltip: {
        backgroundColor: 'rgba(0, 0, 0, 0.8)',
        titleColor: '#fff',
        bodyColor: '#fff',
        borderColor: '#4caf50',
        borderWidth: 2,
        cornerRadius: 8,
        displayColors: false,
        callbacks: {
          title: function(context) {
            return context[0].label;
          },
          label: function(context) {
            const score = context.raw;
            const grade = getGradeFromScore(score);
            return `점수: ${score}점 (${grade})`;
          }
        }
      }
    },
    elements: {
      line: {
        tension: 0.2
      }
    }
  };

  const averageScore = Math.round(ratings.reduce((sum, score) => sum + score, 0) / ratings.length);

  return (
    <ChartContainer>
      <ChartTitle>{title}</ChartTitle>
      
      <ChartWrapper>
        <Radar data={data} options={options} />
      </ChartWrapper>

      <ScoresSummary>
        {labels.map((label, index) => (
          <ScoreItem key={label}>
            <ScoreLabel>{label}</ScoreLabel>
            <ScoreValue>
              <ScoreNumber>{ratings[index]}점</ScoreNumber>
              <ScoreGrade score={ratings[index]}>
                {getGradeFromScore(ratings[index])}
              </ScoreGrade>
            </ScoreValue>
          </ScoreItem>
        ))}
      </ScoresSummary>

      <AverageScore>
        <AverageLabel>종합 평점:</AverageLabel>
        <AverageValue>{averageScore}점</AverageValue>
        <ScoreGrade score={averageScore}>
          {getGradeFromScore(averageScore)}
        </ScoreGrade>
      </AverageScore>
    </ChartContainer>
  );
};

export default RadarChart;