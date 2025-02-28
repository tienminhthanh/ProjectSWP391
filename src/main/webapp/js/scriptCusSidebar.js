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
    
