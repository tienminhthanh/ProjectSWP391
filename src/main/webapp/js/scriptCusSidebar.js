/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


function openMobileMenu(){
    const mobileMenu =  document.getElementById('cus-sidebar');
    const overlay = document.getElementById('cus-sidebar-overlay');
    mobileMenu.style.display = 'block';
    overlay.style.display = 'block';
        
    }
    
function closeMobileMenu(){
    const mobileMenu =  document.getElementById('cus-sidebar');
    const overlay = document.getElementById('cus-sidebar-overlay');
    mobileMenu.style.display = 'none';
    overlay.style.display = 'none';
        
    }
    
    
//    Move this to a separate .js file if we dont need side bar on large view on all screen
    window.addEventListener('resize', () => {
    const clientWidth = document.documentElement.clientWidth;
    const sidebar = document.getElementById('cus-sidebar');
    if (clientWidth > 768) {
        sidebar.style.display = 'block';
    } else {
        sidebar.style.display = 'none';
    }
});
    
