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
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>
        <script src="https://cdn.tailwindcss.com"></script>
        <style>
            canvas {
                font-family: 'Arial', sans-serif !important;
            }
            #orderConversionChart {
                max-height: 250px !important;
                max-width: 300px !important;
                margin: 0 auto;
            }
            #revenueVsProfitChart {
                max-height: 300px !important;
            }
            #ageDistributionChart {
                max-height: 200px !important;
                max-width: 200px !important;
                margin: 0 auto;
            }
            /* Style cho legend tùy chỉnh */
            .custom-legend {
                display: flex;
                flex-direction: column;
                gap: 8px; /* Khoảng cách giữa các mục trong legend */
            }
            .legend-item {
                display: flex;
                align-items: center;
                gap: 8px; /* Khoảng cách giữa màu và nhãn */
            }
            .legend-color {
                width: 20px;
                height: 20px;
                border-radius: 4px;
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

                <!-- Hàng 1: Revenue Trend Chart -->
                <section class="grid grid-cols-1 gap-6">
                    <div class="bg-white p-4 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-black mb-2">Revenue Trend</h2>
                        <form action="dashboard" method="GET" class="mb-4">
                            <label for="revenueTrendYear" class="mr-2">Year:</label>
                            <select name="revenueTrendYear" id="revenueTrendYear" class="border rounded p-2">
                                <c:forEach begin="2024" end="2025" var="y">
                                    <option value="${y}" ${y == selectedRevenueTrendYear ? 'selected' : ''}>${y}</option>
                                </c:forEach>
                            </select>
                            <input type="hidden" name="year" value="${param.year}">
                            <input type="hidden" name="month" value="${param.month}">
                            <button type="submit" class="bg-orange-400 text-white px-4 py-2 rounded ml-2">Filter</button>
                        </form>
                        <canvas id="revenueTrendChart"></canvas>
                    </div>
                </section>

                <!-- Hàng 2: Top 5 Buyers và User Age Distribution -->
                <section class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-6">
                    <!-- Top 5 Buyers -->
                    <div class="bg-white p-4 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-black mb-2">Top 5 Buyers</h2>
                        <c:if test="${empty topBuyers}">
                            <div class="text-center text-gray-500 py-4">
                                No data available
                            </div>
                        </c:if>
                        <c:if test="${not empty topBuyers}">
                            <div class="overflow-x-auto">
                                <table class="w-full text-left border-collapse">
                                    <thead>
                                        <tr class="bg-orange-400 text-white">
                                            <th class="p-2 border text-center w-12">ID</th>
                                            <th class="p-2 border text-center w-32">Username</th>
                                            <th class="p-2 border text-center w-40">First Name</th>
                                            <th class="p-2 border text-center w-40">Last Name</th>
                                            <th class="p-2 border text-center w-48">Total Purchase Points</th>
                                            <th class="p-2 border text-center w-64">Email</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${topBuyers}" var="buyer">
                                            <tr class="hover:bg-gray-100">
                                                <td class="p-2 border text-center">${buyer.accountID}</td>
                                                <td class="p-2 border text-left">${buyer.username}</td>
                                                <td class="p-2 border text-center">${buyer.firstName}</td>
                                                <td class="p-2 border text-center">${buyer.lastName}</td>
                                                <td class="p-2 border text-center">${buyer.totalPurchasePoints}</td>
                                                <td class="p-2 border text-left">${buyer.email}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>
                    </div>
                    <!-- User Age Distribution Pie Chart -->
                    <div class="bg-white p-4 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-black mb-2">User Age Distribution</h2>
                        <div class="flex items-center justify-center gap-6">
                            <!-- Legend tùy chỉnh bên trái -->
                            <div class="custom-legend">
                                <c:set var="colors" value="${['#FF6384', '#36A2EB', '#FFCE56', '#4CAF50', '#9C27B0', '#FF9800']}" />
                                <c:set var="index" value="0" />
                                <c:forEach items="${ageStatistics.keySet()}" var="ageGroup">
                                    <div class="legend-item">
                                        <span class="legend-color" style="background-color: ${colors[index]};"></span>
                                        <span>${ageGroup}</span>
                                    </div>
                                    <c:set var="index" value="${index + 1}" />
                                </c:forEach>
                            </div>
                            <!-- Biểu đồ bên phải -->
                            <div>
                                <canvas id="ageDistributionChart"></canvas>
                            </div>
                        </div>
                    </div>
                </section>

                <!-- Các hàng dưới giữ nguyên -->
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
                        <button type="submit" class="bg-orange-400 text-white px-4 py-2 rounded w-full">Filter</button>
                    </div>
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

                <!-- Total Revenue vs Gross Profit và Order Conversion Rate -->
                <section class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-6">
                    <!-- Total Revenue vs Gross Profit Bar Chart -->
                    <div class="bg-white p-4 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-black mb-2">Total Revenue vs Gross Profit</h2>
                        <c:if test="${totalRevenue == 0 && grossProfit == 0}">
                            <div class="text-center text-gray-500 py-4">
                                No data available for this period
                            </div>
                        </c:if>
                        <div <c:if test="${totalRevenue == 0 && grossProfit == 0}">style="display: none;"</c:if>>
                                <canvas id="revenueVsProfitChart"></canvas>
                            </div>
                        </div>

                        <!-- Order Conversion Rate Gauge Chart -->
                        <div class="bg-white p-4 rounded-lg shadow-lg">
                            <h2 class="text-lg font-semibold text-black mb-2">Order Conversion Rate</h2>
                        <c:if test="${totalOrders == 0}">
                            <div class="text-center text-gray-500 py-4">
                                No orders available for this period
                            </div>
                        </c:if>
                        <div <c:if test="${totalOrders == 0}">style="display: none;"</c:if>>
                                <canvas id="orderConversionChart"></canvas>
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

                <!-- Top 5 Products by Category -->
                <section class="bg-white p-4 rounded-lg shadow-lg mt-6">
                    <h2 class="text-lg font-semibold text-black mb-2">Top 5 Products by Category</h2>
                    <c:if test="${empty topProductsByCategory}">
                        <div class="text-center text-gray-500 py-4">
                            No categories available
                        </div>
                    </c:if>
                    <c:if test="${not empty topProductsByCategory}">
                        <div class="overflow-x-auto">
                            <c:forEach items="${topProductsByCategory}" var="categoryEntry">
                                <h3 class="text-md font-medium text-black mt-4 mb-2">${categoryEntry.key}</h3>
                                <c:if test="${empty categoryEntry.value}">
                                    <div class="text-center text-gray-500 py-4">
                                        No products available
                                    </div>
                                </c:if>
                                <c:if test="${not empty categoryEntry.value}">
                                    <table class="w-full text-left border-collapse mb-4">
                                        <thead>
                                            <tr class="bg-orange-400 text-white">
                                                <th class="p-2 border text-center w-12">ID</th>
                                                <th class="p-2 border text-center w-64">Product Name</th>
                                                <th class="p-2 border text-center w-48">Total Quantity Sold</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${categoryEntry.value}" var="product">
                                                <tr class="hover:bg-gray-100">
                                                    <td class="p-2 border text-center">${product.productID}</td>
                                                    <td class="p-2 border text-center">${product.productName}</td>
                                                    <td class="p-2 border text-center">${quantitySoldMap[product.productID]}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </c:if>
                            </c:forEach>
                        </div>
                    </c:if>
                </section>
            </div>
        </div>

        <c:set var="totalUsers" value="0"/>
        <c:forEach items="${ageStatistics.values()}" var="count">
            <c:set var="totalUsers" value="${totalUsers + count}"/>
        </c:forEach>

        <!-- JavaScript -->
        <script>
            Chart.defaults.font.family = 'Arial, sans-serif';
            Chart.defaults.font.size = 12;
            Chart.defaults.color = '#000000';

            document.addEventListener("DOMContentLoaded", function () {
                // Revenue Trend Chart
                const revenueTrendCtx = document.getElementById('revenueTrendChart').getContext('2d');
                const labels = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"];
                const data = new Array(12).fill(0);
                let month;
            <c:forEach items="${revenueTrend}" var="entry">
                month = parseInt("${entry.key}".split("-")[1]) - 1;
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
                        responsive: true,
                        maintainAspectRatio: true,
                        plugins: {
                            legend: {
                                display: false // Tắt legend mặc định của Chart.js
                            }
                        }
                    }
                });

                // Total Revenue vs Gross Profit Bar Chart
                const revenueVsProfitCtx = document.getElementById('revenueVsProfitChart').getContext('2d');
                new Chart(revenueVsProfitCtx, {
                    type: 'bar',
                    data: {
                        labels: ['Total Revenue', 'Gross Profit', 'Profit Margin'],
                        datasets: [
                            {
                                label: 'Total Revenue (đ)',
                                data: [${totalRevenue}, null, null],
                                backgroundColor: 'rgba(54, 162, 235, 0.6)',
                                borderColor: 'rgba(54, 162, 235, 1)',
                                borderWidth: 1,
                                yAxisID: 'y'
                            },
                            {
                                label: 'Gross Profit (đ)',
                                data: [null, ${grossProfit}, null],
                                backgroundColor: 'rgba(255, 99, 132, 0.6)',
                                borderColor: 'rgba(255, 99, 132, 1)',
                                borderWidth: 1,
                                yAxisID: 'y'
                            },
                            {
                                label: 'Profit Margin (%)',
                                data: [null, null, Number(${profitMargin}).toFixed(2)],
                                backgroundColor: 'rgba(255, 159, 64, 0.6)',
                                borderColor: 'rgba(255, 159, 64, 1)',
                                borderWidth: 1,
                                yAxisID: 'y1'
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
                                        return value.toLocaleString('vi-VN');
                                    }
                                }
                            },
                            y1: {
                                position: 'right',
                                beginAtZero: true,
                                max: 100,
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
                                        return value + '%';
                                    }
                                },
                                grid: {
                                    drawOnChartArea: false
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
                                display: true
                            },
                            tooltip: {
                                callbacks: {
                                    label: function (context) {
                                        if (context.dataset.label === 'Profit Margin (%)') {
                                            return context.dataset.label + ': ' + Number(context.parsed.y).toFixed(2) + '%';
                                        }
                                        return context.dataset.label + ': ' + context.parsed.y.toLocaleString('vi-VN') + ' đ';
                                    }
                                }
                            },
                            datalabels: {
                                display: false
                            }
                        }
                    },
                    plugins: [ChartDataLabels]
                });

                // Order Conversion Rate Gauge Chart
                const orderConversionCtx = document.getElementById('orderConversionChart').getContext('2d');
                const orderConversionRate = Number(${orderConversionRate}).toFixed(2);
                console.log("Order Conversion Rate (raw):", ${orderConversionRate});
                console.log("Order Conversion Rate (converted):", orderConversionRate);

                new Chart(orderConversionCtx, {
                    type: 'doughnut',
                    data: {
                        labels: ['Successful', 'Fail'],
                        datasets: [{
                                data: [orderConversionRate, 100 - orderConversionRate],
                                backgroundColor: [
                                    orderConversionRate >= 80 ? 'rgba(75, 192, 192, 0.6)' : orderConversionRate >= 50 ? 'rgba(255, 206, 86, 0.6)' : 'rgba(255, 99, 132, 0.6)',
                                    'rgba(200, 200, 200, 0.2)'
                                ],
                                borderColor: [
                                    orderConversionRate >= 80 ? 'rgba(75, 192, 192, 1)' : orderConversionRate >= 50 ? 'rgba(255, 206, 86, 1)' : 'rgba(255, 99, 132, 1)',
                                    'rgba(200, 200, 200, 0.5)'
                                ],
                                borderWidth: 1,
                                circumference: 180,
                                rotation: 270
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: true,
                        cutout: '70%',
                        plugins: {
                            legend: {
                                display: true,
                                position: 'bottom',
                                labels: {
                                    color: '#000000',
                                    font: {
                                        family: 'Arial',
                                        size: 12
                                    },
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
                                enabled: false
                            }
                        }
                    }
                });
            });
        </script>
    </body>
</html>