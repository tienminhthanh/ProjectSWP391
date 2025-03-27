<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="en_US"/>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sales Dashboard</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script> <!-- Added ChartDataLabels plugin -->
        <script src="https://cdn.tailwindcss.com"></script>
        <style>
            /* Đảm bảo canvas của Chart.js không bị ảnh hưởng bởi Tailwind CSS */
            canvas {
                font-family: 'Arial', sans-serif !important;
            }
            /* Làm nhỏ kích thước biểu đồ Order Conversion Rate */
            #orderConversionChart {
                max-height: 250px !important; /* Giảm chiều cao */
                max-width: 300px !important; /* Giảm chiều rộng */
                margin: 0 auto; /* Căn giữa biểu đồ */
            }
            /* Điều chỉnh kích thước biểu đồ Total Revenue vs Gross Profit nếu cần */
            #revenueVsProfitChart {
                max-height: 300px !important; /* Đảm bảo đồng bộ chiều cao */
            }
        </style>
    </head>
    <body class="bg-gray-50 min-h-screen flex">
        <!-- Sidebar -->
        <aside class="w-64 bg-orange-400 text-white min-h-screen fixed left-0">
            <jsp:include page="navbarAdmin.jsp" flush="true"/>
        </aside>

        <div class="flex-1 p-6 ml-64">
            <!-- Main Content -->
            <div class="w-full max-w-full bg-white p-8 shadow-lg rounded-lg">
                <h1 class="text-3xl font-bold text-gray-800 mb-6">📊 Sales Dashboard</h1>
                <!-- Charts -->
                <section class="grid grid-cols-1 md:grid-cols-[2fr_1fr] gap-6">
                    <!-- Revenue Trend Chart -->
                    <div class="bg-white p-4 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-black mb-2">Revenue Trend</h2>
                        <!-- Bộ lọc năm cho Revenue Trend -->
                        <form action="dashboard" method="GET" class="mb-4">
                            <label for="revenueTrendYear" class="mr-2">Year:</label>
                            <select name="revenueTrendYear" id="revenueTrendYear" class="border rounded p-2">
                                <c:forEach begin="2024" end="2025" var="y">
                                    <option value="${y}" ${y == selectedRevenueTrendYear ? 'selected' : ''}>${y}</option>
                                </c:forEach>
                            </select>
                            <!-- Giữ các tham số khác để không bị mất khi submit -->
                            <input type="hidden" name="year" value="${param.year}">
                            <input type="hidden" name="month" value="${param.month}">
                            <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded ml-2">Filter</button>
                        </form>
                        <canvas id="revenueTrendChart"></canvas>
                    </div>
                    <!-- Age Distribution Pie Chart -->
                    <div class="bg-white p-4 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-black mb-2">User Age Distribution</h2>
                        <canvas id="ageDistributionChart"></canvas>
                    </div>
                </section>

                <!-- Bộ lọc chính -->
                <form action="dashboard" method="GET" class="mb-6 mt-6 grid grid-cols-3 gap-4">
                    <div>
                        <label for="year" class="mr-2">Year:</label>
                        <select name="year" id="year" class="border rounded p-2 w-full">
                            <c:forEach begin="2024" end="2025" var="y">
                                <option value="${y}" ${y == (selectedYear != null ? selectedYear : 2024) ? 'selected' : ''}>${y}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div>
                        <label for="month" class="mr-2">Month:</label>
                        <select name="month" id="month" class="border rounded p-2 w-full">
                            <option value="">All</option>
                            <c:set var="monthNames" value="${['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']}" />
                            <c:forEach begin="1" end="12" var="m">
                                <option value="${m}" ${m == selectedMonth ? 'selected' : ''}>${monthNames[m-1]}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="flex items-end">
                        <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded w-full">Filter</button>
                    </div>
                    <!-- Giữ revenueTrendYear khi submit bộ lọc chính -->
                    <input type="hidden" name="revenueTrendYear" value="${param.revenueTrendYear}">
                </form>

                <!-- Overview Cards -->
                <section class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-6">
                    <div class="bg-white p-6 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-black">Total Revenue</h2>
                        <p class="text-3xl font-bold text-blue-600">
                            <fmt:formatNumber value="${totalRevenue}" type="number" groupingUsed="true" minFractionDigits="2" maxFractionDigits="2"/> đ
                        </p>
                    </div>
                    <div class="bg-white p-6 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-black">Total Quantity Sold</h2>
                        <p class="text-3xl font-bold text-green-600">${totalQuantitySold}</p>
                    </div>
                    <div class="bg-white p-6 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-black">Order Conversion Rate</h2>
                        <p class="text-3xl font-bold text-purple-600">
                            <fmt:formatNumber value="${orderConversionRate}" type="number" pattern="0.00"/>%
                        </p>
                    </div>
                    <div class="bg-white p-6 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-black">Gross Profit</h2>
                        <p class="text-3xl font-bold text-orange-600">
                            <fmt:formatNumber value="${grossProfit}" type="number" groupingUsed="true" minFractionDigits="2" maxFractionDigits="2"/> đ
                        </p>
                    </div>
                    <div class="bg-white p-6 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-black">Profit Margin</h2>
                        <p class="text-3xl font-bold text-red-600">
                            <fmt:formatNumber value="${profitMargin}" type="number" pattern="0.00"/>%
                        </p>
                    </div>
                </section>

                <!-- Total Revenue vs Gross Profit và Order Conversion Rate trong cùng một hàng -->
                <section class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-6">
                    <!-- Total Revenue vs Gross Profit Bar Chart -->
                    <div class="bg-white p-4 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-black mb-2">Total Revenue vs Gross Profit</h2>
                        <!-- Hiển thị thông báo nếu không có dữ liệu -->
                        <c:if test="${totalRevenue == 0 && grossProfit == 0}">
                            <div class="text-center text-gray-500 py-4">
                                No data available for this period
                            </div>
                        </c:if>
                        <!-- Hiển thị biểu đồ nếu có dữ liệu -->
                        <div <c:if test="${totalRevenue == 0 && grossProfit == 0}">style="display: none;"</c:if>>
                                <canvas id="revenueVsProfitChart"></canvas>
                            </div>
                        </div>

                        <!-- Order Conversion Rate Gauge Chart -->
                        <div class="bg-white p-4 rounded-lg shadow-lg">
                            <h2 class="text-lg font-semibold text-black mb-2">Order Conversion Rate</h2>
                            <!-- Hiển thị thông báo nếu không có dữ liệu -->
                        <c:if test="${totalOrders == 0}">
                            <div class="text-center text-gray-500 py-4">
                                No orders available for this period
                            </div>
                        </c:if>
                        <!-- Hiển thị biểu đồ nếu có dữ liệu -->
                        <div <c:if test="${totalOrders == 0}">style="display: none;"</c:if>>
                                <canvas id="orderConversionChart"></canvas>
                                <!-- Hiển thị giá trị Order Conversion Rate và thông tin chi tiết bên dưới -->
                                <div class="text-center mt-2">
                                    <div class="text-lg font-bold text-purple-600">
                                    <fmt:formatNumber value="${orderConversionRate}" type="number" pattern="0.00"/>%
                                </div>
                                <div class="text-sm text-gray-600 mt-1">
                                    <span class="font-semibold">Total orders:</span> <span class="text-gray-800">${totalOrders}</span>
                                </div>
                                <div class="text-sm text-gray-600">
                                    <span class="font-semibold">Number of successful orders:</span> <span class="text-teal-600">${successfulOrders}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </div>
        <c:set var="totalUsers" value="0"/>
        <c:forEach items="${ageStatistics.values()}" var="count">
            <c:set var="totalUsers" value="${totalUsers + count}"/>
        </c:forEach>

        <!-- JavaScript -->
        <script>
            // Đặt font mặc định cho toàn bộ Chart.js
            Chart.defaults.font.family = 'Arial, sans-serif';
            Chart.defaults.font.size = 12;
            Chart.defaults.color = '#000000';

            // Gọi hàm khi trang được tải
            document.addEventListener("DOMContentLoaded", function () {
                // Revenue Trend Chart
                const revenueTrendCtx = document.getElementById('revenueTrendChart').getContext('2d');
                const labels = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"];
                const data = new Array(12).fill(0);
            <c:forEach items="${revenueTrend}" var="entry">
                const month = parseInt("${entry.key}".split("-")[1]) - 1;
                data[month] = ${entry.value};
            </c:forEach>

                new Chart(revenueTrendCtx, {
                    type: 'line',
                    data: {
                        labels: labels,
                        datasets: [{
                                label: 'Revenue',
                                data: data,
                                borderColor: 'rgba(54, 162, 235, 1)',
                                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                                borderWidth: 2,
                                fill: true
                            }]
                    },
                    options: {
                        responsive: true,
                        scales: {
                            y: {
                                beginAtZero: true,
                                title: {
                                    display: true,
                                    text: 'Revenue (đ)',
                                    color: '#000000',
                                    font: {
                                        family: 'Arial',
                                        size: 14,
                                        weight: 'bold'
                                    }
                                },
                                ticks: {
                                    color: '#000000',
                                    font: {
                                        family: 'Arial',
                                        size: 12
                                    }
                                }
                            },
                            x: {
                                title: {
                                    display: true,
                                    text: 'Month',
                                    color: '#000000',
                                    font: {
                                        family: 'Arial',
                                        size: 14,
                                        weight: 'bold'
                                    }
                                },
                                ticks: {
                                    color: '#000000',
                                    font: {
                                        family: 'Arial',
                                        size: 12
                                    }
                                }
                            }
                        }
                    }
                });

                // Age Distribution Pie Chart
                const ageCtx = document.getElementById('ageDistributionChart').getContext('2d');
                new Chart(ageCtx, {
                    type: 'pie',
                    data: {
                        labels: [<c:forEach items="${ageStatistics.keySet()}" var="ageGroup" varStatus="loop">"${ageGroup}"<c:if test="${!loop.last}">,</c:if></c:forEach>],
                                datasets: [{
                                        data: [<c:forEach items="${ageStatistics.values()}" var="count" varStatus="loop">${count}<c:if test="${!loop.last}">,</c:if></c:forEach>],
                                        backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4CAF50', '#9C27B0', '#FF9800'],
                                        hoverBackgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4CAF50', '#9C27B0', '#FF9800']
                                    }]
                    },
                    options: {
                        plugins: {
                            legend: {
                                labels: {
                                    color: '#000000',
                                    font: {
                                        family: 'Arial',
                                        size: 12
                                    }
                                }
                            }
                        }
                    }
                });

              // Total Revenue vs Gross Profit Bar Chart (with Profit Margin)
const revenueVsProfitCtx = document.getElementById('revenueVsProfitChart').getContext('2d');
new Chart(revenueVsProfitCtx, {
    type: 'bar',
    data: {
        labels: ['Total Revenue', 'Gross Profit', 'Profit Margin'],
        datasets: [
            {
                label: 'Total Revenue (đ)', // Separate label for Total Revenue
                data: [${totalRevenue}, null, null], // Only show Total Revenue
                backgroundColor: 'rgba(54, 162, 235, 0.6)', // Blue for Total Revenue
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1,
                yAxisID: 'y' // Link to the first y-axis (for revenue and profit)
            },
            {
                label: 'Gross Profit (đ)', // Separate label for Gross Profit
                data: [null, ${grossProfit}, null], // Only show Gross Profit
                backgroundColor: 'rgba(255, 99, 132, 0.6)', // Pink for Gross Profit
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1,
                yAxisID: 'y' // Link to the first y-axis (for revenue and profit)
            },
            {
                label: 'Profit Margin (%)',
                data: [null, null, Number(${profitMargin}).toFixed(2)], // Format Profit Margin to 2 decimal places
                backgroundColor: 'rgba(255, 159, 64, 0.6)', // Orange color for Profit Margin
                borderColor: 'rgba(255, 159, 64, 1)',
                borderWidth: 1,
                yAxisID: 'y1' // Link to the second y-axis (for percentage)
            }
        ]
    },
    options: {
        responsive: true,
        scales: {
            y: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: 'Amount (đ)',
                    color: '#000000',
                    font: {
                        family: 'Arial',
                        size: 14,
                        weight: 'bold'
                    }
                },
                ticks: {
                    color: '#000000',
                    font: {
                        family: 'Arial',
                        size: 12
                    },
                    callback: function (value) {
                        return value.toLocaleString('vi-VN'); // Show only the number (e.g., 10,000, 20,000) on the y-axis
                    }
                }
            },
            y1: {
                position: 'right', // Secondary y-axis on the right for Profit Margin
                beginAtZero: true,
                max: 100, // Since Profit Margin is a percentage, set max to 100
                title: {
                    display: true,
                    text: 'Profit Margin (%)',
                    color: '#000000',
                    font: {
                        family: 'Arial',
                        size: 14,
                        weight: 'bold'
                    }
                },
                ticks: {
                    color: '#000000',
                    font: {
                        family: 'Arial',
                        size: 12
                    },
                    callback: function (value) {
                        return value + '%'; // Add % symbol for Profit Margin
                    }
                },
                grid: {
                    drawOnChartArea: false // Avoid overlapping grid lines with the left y-axis
                }
            },
            x: {
                title: {
                    display: true,
                    text: 'Metrics',
                    color: '#000000',
                    font: {
                        family: 'Arial',
                        size: 14,
                        weight: 'bold'
                    }
                },
                ticks: {
                    color: '#000000',
                    font: {
                        family: 'Arial',
                        size: 12
                    }
                }
            }
        },
        plugins: {
            legend: {
                display: true // Show legend to distinguish between datasets
            },
            tooltip: {
                callbacks: {
                    label: function (context) {
                        if (context.dataset.label === 'Profit Margin (%)') {
                            return context.dataset.label + ': ' + Number(context.parsed.y).toFixed(2) + '%'; // Format to 2 decimal places
                        }
                        return context.dataset.label + ': ' + context.parsed.y.toLocaleString('vi-VN') + ' đ';
                    }
                }
            },
            datalabels: {
                display: false // Disable data labels on top of the bars
            }
        }
    },
    plugins: [ChartDataLabels] // Include the ChartDataLabels plugin
});

                // Order Conversion Rate Gauge Chart (dựa trên Doughnut Chart)
                const orderConversionCtx = document.getElementById('orderConversionChart').getContext('2d');
                const orderConversionRate = Number(${orderConversionRate}).toFixed(2); // Format to 2 decimal places
                console.log("Order Conversion Rate (raw):", ${orderConversionRate});
                console.log("Order Conversion Rate (converted):", orderConversionRate);

                new Chart(orderConversionCtx, {
                    type: 'doughnut',
                    data: {
                        labels: ['Successful', 'Fail'], // Nhãn cho legend
                        datasets: [{
                                data: [orderConversionRate, 100 - orderConversionRate], // Use formatted value
                                backgroundColor: [
                                    orderConversionRate >= 80 ? 'rgba(75, 192, 192, 0.6)' : orderConversionRate >= 50 ? 'rgba(255, 206, 86, 0.6)' : 'rgba(255, 99, 132, 0.6)', // Màu thay đổi theo giá trị
                                    'rgba(200, 200, 200, 0.2)' // Màu nền cho phần còn lại
                                ],
                                borderColor: [
                                    orderConversionRate >= 80 ? 'rgba(75, 192, 192, 1)' : orderConversionRate >= 50 ? 'rgba(255, 206, 86, 1)' : 'rgba(255, 99, 132, 1)',
                                    'rgba(200, 200, 200, 0.5)'
                                ],
                                borderWidth: 1,
                                circumference: 180, // Chỉ vẽ nửa vòng tròn (gauge chart)
                                rotation: 270 // Xoay để bắt đầu từ dưới
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: true,
                        cutout: '70%', // Tạo lỗ ở trung tâm
                        plugins: {
                            legend: {
                                display: true, // Hiển thị legend
                                position: 'bottom',
                                labels: {
                                    color: '#000000',
                                    font: {
                                        family: 'Arial',
                                        size: 12
                                    },
                                    // Tùy chỉnh màu sắc trong legend
                                    generateLabels: function (chart) {
                                        const data = chart.data;
                                        return data.labels.map((label, index) => {
                                            const backgroundColor = data.datasets[0].backgroundColor[index];
                                            return {
                                                text: label,
                                                fillStyle: backgroundColor,
                                                strokeStyle: data.datasets[0].borderColor[index],
                                                lineWidth: 1,
                                                hidden: false,
                                                index: index
                                            };
                                        });
                                    }
                                }
                            },
                            tooltip: {
                                enabled: false // Ẩn tooltip
                            }
                        }
                    }
                });
            });
        </script>
    </body>
</html>