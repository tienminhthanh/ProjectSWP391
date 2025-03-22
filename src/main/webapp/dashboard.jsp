<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sales Dashboard</title>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-gray-50 min-h-screen flex">
        <!-- Sidebar -->
        <aside class="w-64 bg-orange-400 text-white min-h-screen fixed left-0">
            <jsp:include page="navbarAdmin.jsp" flush="true"/>
        </aside>

        <!-- Main Content -->
        <main class="ml-64 p-6 flex-1">
            <h1 class="text-3xl font-bold text-gray-800 mb-6">📊 Sales Dashboard</h1>

            <!-- Overview Cards -->
            <section class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-6">
                <div class="bg-white p-6 rounded-lg shadow-lg">
                    <h2 class="text-lg font-semibold text-gray-700">Total Revenue</h2>
                    <p class="text-3xl font-bold text-blue-600">$${totalRevenue}</p>
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
                    <p class="text-3xl font-bold text-orange-600">$${grossProfit}</p>
                </div>
                <div class="bg-white p-6 rounded-lg shadow-lg">
                    <h2 class="text-lg font-semibold text-gray-700">Profit Margin</h2>
                    <p class="text-3xl font-bold text-red-600">${profitMargin}%</p>
                </div>
            </section>

            <!-- Charts -->
            <section class="space-y-6">
                <!-- Revenue Trend Chart -->
                <div class="bg-white p-6 rounded-lg shadow-lg">
                    <h2 class="text-xl font-semibold text-gray-700 mb-4">Revenue Trend</h2>
                    <canvas id="revenueTrendChart"></canvas>
                </div>

                <!-- Age Statistics Chart -->
                <div class="bg-white p-6 rounded-lg shadow-lg">
                    <h2 class="text-xl font-semibold text-gray-700 mb-4">Customer Age Distribution</h2>
                    <canvas id="ageStatsChart"></canvas>
                </div>
            </section>
        </main>

        <!-- JavaScript -->
        <script>
            // Revenue Trend Chart
            const revenueTrendCtx = document.getElementById('revenueTrendChart').getContext('2d');
            new Chart(revenueTrendCtx, {
            type: 'line',
                    data: {
                    labels: [<c:forEach items="${revenueTrend.keySet()}" var="month" varStatus="loop">"${month}"<c:if test="${!loop.last}">,</c:if></c:forEach>],
                            datasets: [{
                            label: 'Revenue',
                                    data: [<c:forEach items="${revenueTrend.values()}" var="revenue" varStatus="loop">${revenue}<c:if test="${!loop.last}">,</c:if></c:forEach>],
                                    borderColor: 'rgba(54, 162, 235, 1)',
                                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                                    borderWidth: 2,
                                    fill: true
                            }]
                    },
                    options: {
                    responsive: true,
                            scales: {
                            y: { beginAtZero: true }
                            }
                    }
            });
            // Age Statistics Pie Chart
            const ageStatsCtx = document.getElementById('ageStatsChart').getContext('2d');
            new Chart(ageStatsCtx, {
            type: 'pie',
                    data: {
                    labels: [<c:forEach items="${ageStats.keySet()}" var="ageGroup" varStatus="loop">"${ageGroup}"<c:if test="${!loop.last}">,</c:if></c:forEach>],
                            datasets: [{
                            data: [<c:forEach items="${ageStats.values()}" var="count" varStatus="loop">${count}<c:if test="${!loop.last}">,</c:if></c:forEach>],
                                    backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4CAF50', '#9C27B0'],
                                    borderWidth: 1
                            }]
                    },
                    options: {
                    responsive: true
                    }
            });
        </script>
    </body>
</html>
