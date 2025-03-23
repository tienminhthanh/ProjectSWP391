<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sales Dashboard</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://cdn.tailwindcss.com"></script>
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

                <!-- Bộ lọc -->
                <form action="dashboard" method="GET" class="mb-6 grid grid-cols-2 gap-4">
                    <div>
                        <label for="year">Năm:</label>
                        <input type="text" id="year" name="year" value="${param.year}" class="border rounded p-2 w-full">
                    </div>
                    <div>
                        <label for="month">Tháng:</label>
                        <input type="text" id="month" name="month" value="${param.month}" class="border rounded p-2 w-full">
                    </div>
                    <div>
                        <label for="week">Tuần:</label>
                        <input type="text" id="week" name="week" value="${param.week}" class="border rounded p-2 w-full">
                    </div>
                    <div>
                        <label for="day">Ngày:</label>
                        <input type="text" id="day" name="day" value="${param.day}" class="border rounded p-2 w-full">
                    </div>
                    <div>
                        <label for="timeFrame">Loại thống kê:</label>
                        <select name="timeFrame" class="border rounded p-2 w-full">
                            <option value="month" ${param.timeFrame == 'month' ? 'selected' : ''}>Theo tháng</option>
                            <option value="quarter" ${param.timeFrame == 'quarter' ? 'selected' : ''}>Theo quý</option>
                            <option value="year" ${param.timeFrame == 'year' ? 'selected' : ''}>Theo năm</option>
                        </select>
                    </div>
                    <div class="flex items-end">
                        <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded w-full">Lọc</button>
                    </div>
                </form>

                <!-- Overview Cards -->
                <section class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-6">
                    <div class="bg-white p-6 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-gray-700">Total Revenue</h2>
                        <p class="text-3xl font-bold text-blue-600">
                            <fmt:formatNumber value="${totalRevenue}" type="number" groupingUsed="true" minFractionDigits="2" maxFractionDigits="2"/> đ
                        </p>
                    </div>
                    <div class="bg-white p-6 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-gray-700">Total Quantity Sold</h2>
                        <p class="text-3xl font-bold text-green-600">${totalQuantitySold}</p>
                    </div>
                    <div class="bg-white p-6 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-gray-700">Order Conversion Rate</h2>
                        <p class="text-3xl font-bold text-purple-600">${orderConversionRate}%</p>
                    </div>
                    <div class="bg-white p-6 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-gray-700">Gross Profit</h2>
                        <p class="text-3xl font-bold text-orange-600">
                            <fmt:formatNumber value="${grossProfit}" type="number" groupingUsed="true" minFractionDigits="2" maxFractionDigits="2"/> đ
                        </p>
                    </div>
                    <div class="bg-white p-6 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-gray-700">Profit Margin</h2>
                        <p class="text-3xl font-bold text-red-600">${profitMargin}%</p>
                    </div>
                </section>

                <!-- Charts -->
                <section class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- Revenue Trend Chart -->
                    <div class="bg-white p-4 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-gray-700 mb-2">Revenue Trend</h2>
                        <canvas id="revenueTrendChart"></canvas>
                    </div>
                    <!-- Age Distribution Pie Chart -->
                    <div class="bg-white p-4 rounded-lg shadow-lg">
                        <h2 class="text-lg font-semibold text-gray-700 mb-2">User Age Distribution</h2>
                        <canvas id="ageDistributionChart"></canvas>
                    </div>

                </section>
            </div>
        </div>

        <!-- JavaScript -->
        <script>
            // Revenue Trend Chart
            document.addEventListener("DOMContentLoaded", function () {
            const revenueTrendCtx = document.getElementById('revenueTrendChart').getContext('2d');
            const labels = [<c:forEach items="${revenueTrend.keySet()}" var="period" varStatus="loop">"${period}"<c:if test="${!loop.last}">,</c:if></c:forEach>];
            const data = [<c:forEach items="${revenueTrend.values()}" var="revenue" varStatus="loop">${revenue}<c:if test="${!loop.last}">,</c:if></c:forEach>];
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
                            y: {beginAtZero: true}
                            }
                    }
            });
            });
                </script>
                <script>
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
                            }
                    });
        </script>

    </body>
</html>
