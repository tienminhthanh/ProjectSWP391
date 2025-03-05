/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


window.addEventListener('resize', () => {
    const clientWidth = document.documentElement.clientWidth;
    const sidebar = document.getElementById('cus-sidebar');
    if (clientWidth > 768) {
        sidebar.style.display = 'block';
    } else {
        sidebar.style.display = 'none';
    }
});