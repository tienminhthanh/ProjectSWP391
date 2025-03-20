<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Account Statistics</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body class="bg-gray-50 min-h-screen flex">
        <div class="w-64 bg-orange-400 text-white min-h-screen fixed left-30">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>


        <!-- Main Content -->
        <div class="flex-1 ml-64 p-6">
            <h1 class="text-3xl font-bold text-gray-800 mb-6">📊 Account Statistics Dashboard</h1>
            <hr class="mb-6 border-gray-300"/>

            <!-- Error Message -->
            <c:if test="${not empty errorMessage}">
                <p class="text-red-600 text-center mt-4 text-sm font-semibold p-2 border border-red-500 rounded bg-red-100 w-full">
                    <i class="fas fa-exclamation-circle mr-2"></i>${errorMessage}
                </p>
            </c:if>

            <!-- Overview Cards -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
                <div class="bg-white p-6 rounded-lg shadow-md">
                    <h2 class="text-lg font-semibold text-gray-700">Total Accounts</h2>
                    <p class="text-3xl font-bold text-blue-600">${totalAccounts}</p>
                </div>
                <div class="bg-white p-6 rounded-lg shadow-md">
                    <h2 class="text-lg font-semibold text-gray-700">Active Accounts</h2>
                    <p class="text-3xl font-bold text-green-600">${activeAccounts}</p>
                </div>
                <div class="bg-white p-6 rounded-lg shadow-md">
                    <h2 class="text-lg font-semibold text-gray-700">Locked Accounts</h2>
                    <p class="text-3xl font-bold text-red-600">${lockedAccounts}</p>
                </div>
                <div class="bg-white p-6 rounded-lg shadow-md">
                    <h2 class="text-lg font-semibold text-gray-700">New Accounts (Month)</h2>
                    <p class="text-3xl font-bold text-purple-600">${newAccountsMonth}</p>
                </div>
            </div>

            <!-- New Accounts Stats -->
            <div class="bg-white p-6 rounded-lg shadow-md mb-6">
                <h2 class="text-xl font-semibold text-gray-700 mb-4">New Accounts Overview</h2>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div class="text-center">
                        <p class="text-lg text-gray-600">Today</p>
                        <p class="text-2xl font-bold text-blue-500">${newAccountsDay}</p>
                    </div>
                    <div class="text-center">
                        <p class="text-lg text-gray-600">This Week</p>
                        <p class="text-2xl font-bold text-blue-500">${newAccountsWeek}</p>
                    </div>
                    <div class="text-center">
                        <p class="text-lg text-gray-600">This Month</p>
                        <p class="text-2xl font-bold text-blue-500">${newAccountsMonth}</p>
                    </div>
                </div>
            </div>

            <!-- Charts -->
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <!-- Bar Chart: Accounts by Role -->
                <div class="bg-white p-6 rounded-lg shadow-md">
                    <h2 class="text-xl font-semibold text-gray-700 mb-4">Accounts by Role (Bar)</h2>
                    <canvas id="accountsByRoleBarChart"></canvas>
                </div>

                <!-- Pie Chart: Accounts by Role -->
                <div class="bg-white p-6 rounded-lg shadow-md">
                    <h2 class="text-xl font-semibold text-gray-700 mb-4">Accounts by Role (Pie)</h2>
                    <canvas id="accountsByRolePieChart"></canvas>
                </div>

                <!-- Line Chart: Account Growth -->
                <div class="bg-white p-6 rounded-lg shadow-md">
                    <h2 class="text-xl font-semibold text-gray-700 mb-4">Account Growth (1 Months)</h2>
                    <canvas id="accountGrowthChart"></canvas>
                </div>
            </div>
        </div>

        <script>
            // Bar Chart: Accounts by Role
            const accountsByRoleBarCtx = document.getElementById('accountsByRoleBarChart').getContext('2d');
            new Chart(accountsByRoleBarCtx, {
            type: 'bar',
                    data: {
                    labels: [<c:forEach items="${accountsByRole.keySet()}" var="role">'${role}',</c:forEach>],
                            datasets: [{
                            label: 'Number of Accounts',
                                    data: [<c:forEach items="${accountsByRole.values()}" var="count">${count},</c:forEach>],
                                    backgroundColor: [
                                            'rgba(255, 99, 132, 0.8)',
                                            'rgba(255, 206, 86, 0.8)',
                                            'rgba(75, 192, 192, 0.8)',
                                            'rgba(153, 102, 255, 0.8)'
                                    ]
                            }]
                    },
                    options: {
                    responsive: true,
                            scales: {
                            y: { beginAtZero: true }
                            }
                    }
            });
            // Pie Chart: Accounts by Role
            const accountsByRolePieCtx = document.getElementById('accountsByRolePieChart').getContext('2d');
            new Chart(accountsByRolePieCtx, {
            type: 'pie',
                    data: {
                    labels: [<c:forEach items="${accountsByRole.keySet()}" var="role">'${role}',</c:forEach>],
                            datasets: [{
                            data: [<c:forEach items="${accountsByRole.values()}" var="count">${count},</c:forEach>],
                                    backgroundColor: [
                                            'rgba(255, 99, 132, 0.8)',
                                            'rgba(255, 206, 86, 0.8)',
                                            'rgba(75, 192, 192, 0.8)',
                                            'rgba(153, 102, 255, 0.8)'
                                    ]
                            }]
                    },
                    options: {
                    responsive: true
                    }
            });
            // Line Chart: Account Growth
            const accountGrowthCtx = document.getElementById('accountGrowthChart').getContext('2d');
            new Chart(accountGrowthCtx, {
            type: 'line',
                    data: {
                    labels: [<c:forEach items="${monthlyGrowth.keySet()}" var="month"> '${month}', </c:forEach>].reverse(), // Reverse the order of months
                            datasets: [{
                            label: 'New Accounts',
                                    data: [<c:forEach items="${monthlyGrowth.values()}" var="count"> ${count}, </c:forEach>].reverse(), // Reverse the order of data points
                                    borderColor: 'rgba(75, 192, 192, 1)',
                                    tension: 0.1,
                                    fill: false
                            }]
                    },
                    options: {
                    responsive: true,
                            scales: {
                            y: { beginAtZero: true }
                            }
                    }
            });

        </script>
    </body>
</html>