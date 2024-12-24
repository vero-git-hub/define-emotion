async function fetchChartData() {
    const response = await fetch('/emotions/chart-data');
    return response.json();
}

async function renderChart() {
    const data = await fetchChartData();

    const moodCounts = data.reduce((acc, emotion) => {
        acc[emotion.mood] = (acc[emotion.mood] || 0) + 1;
        return acc;
    }, {});

    const ctx = document.getElementById('emotionChart').getContext('2d');
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: Object.keys(moodCounts),
            datasets: [{
                label: 'Emotions',
                data: Object.values(moodCounts),
                backgroundColor: ['#FFB6C1', '#FF6347', '#FFD700', '#ADFF2F', '#87CEEB'],
                borderColor: '#FFFFFF',
                borderWidth: 2,
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                tooltip: {
                    callbacks: {
                        label: function(tooltipItem) {
                            const total = tooltipItem.dataset.data.reduce((sum, value) => sum + value, 0);
                            const value = tooltipItem.raw;
                            const percentage = ((value / total) * 100).toFixed(2);
                            return `${tooltipItem.label}: ${value} (${percentage}%)`;
                        }
                    }
                }
            },
            animation: {
                animateScale: true,
                animateRotate: true
            }
        }
    });
}

renderChart();