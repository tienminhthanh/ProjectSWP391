<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Dashboard</title>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <style>
            canvas {
                max-width: 500px;
                max-height: 300px;
            }
        </style>
    </head>
    <body>
        <h1>Sales Dashboard</h1>

        <!-- Bi?u ?? Demographics -->
        <h2>Demographics Distribution</h2>
        <canvas id="demographicsChart"></canvas>

        <!-- B?ng Top s?n ph?m -->
        <h2>Top Selling Products</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>Rank</th>
                    <th>Product Name</th>
                    <th>Category Name</th>
                    <th>Total Sold</th>
                </tr>
            </thead>
            <tbody id="topProductsTable">
            </tbody>
        </table>

        <script>
            $(document).ready(function () {
                $.ajax({
                    url: "http://localhost:8080/dashboard",
                    method: "GET",
                    dataType: "json",
                    success: function (response) {
                        console.log("D? li?u nh?n ???c:", response);

                        // ? Hi?n th? Demographics
                        if (response.demographicsData.length > 0) {
                            const labels = response.demographicsData.map(item => item.ageGroup);
                            const values = response.demographicsData.map(item => item.total);
                            const ctx = document.getElementById("demographicsChart").getContext("2d");

                            new Chart(ctx, {
                                type: "pie",
                                data: {
                                    labels: labels,
                                    datasets: [{
                                            data: values,
                                            backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56", "#4CAF50"]
                                        }]
                                }
                            });
                        }

                        // ? Hi?n th? danh sách s?n ph?m bán ch?y
                        let tableHTML = "";
                        response.topSellingProducts.forEach((product, index) => {
                            tableHTML += `
                                <tr>
                                    <td>${index + 1}</td>
                                    <td>${product.productName}</td>
                                    <td>${product.categoryName}</td>
                                    <td>${product.totalSold}</td>
                                </tr>
                            `;
                        });
                        $("#topProductsTable").html(tableHTML);
                    },
                    error: function (xhr, status, error) {
                        console.error("L?i khi l?y d? li?u t? server:", error);
                    }
                });
            });
        </script>
    </body>
</html>
