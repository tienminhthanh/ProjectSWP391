<%-- chat.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<style>
    .chat-container {
        width: 65%;
        height: 70%;
        position: fixed;
        bottom: 0;
        right: 0;
        display: none;
        background: white;
        box-shadow: -2px -2px 15px rgba(0,0,0,0.1);
        border-radius: 10px 10px 0 0;
        z-index: 1000;
    }

    .chat-container.show {
        display: flex;
    }

    /* Responsive design */
    @media (max-width: 768px) {
        .chat-container {
            width: 100%;
            height: 90%;
        }
    }
</style>
<!-- Chat Button -->
<div class="fixed bottom-4 right-4">
    <button id="chatButton" class="bg-yellow-500 text-white p-2 rounded-full shadow-lg">
        <i class="fas fa-comments"></i>
        <span class="ml-1">Chat</span>
    </button>
</div>

<!-- Chat Container -->
<div id="chatContainer" class="chat-container">
    <!-- Chat Sidebar -->
    <div class="w-1/4 bg-white border-r p-4 flex flex-col">
        <div class="flex justify-between items-center mb-4">
            <h2 class="text-lg font-bold">Chat</h2>
            <button id="closeChat" class="text-red-500 text-xl hover:text-red-700">&times;</button>
        </div>

        <div class="mb-4">
            <input type="text" placeholder="Tìm theo tên..." class="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
        </div>

        <!-- Chat List -->
        <div class="flex-1 overflow-y-auto">
            <c:forEach var="chat" items="${chats}">
                <div class="p-3 flex items-center hover:bg-gray-50 cursor-pointer border-b">
                    <img src="${chat.image}" alt="${chat.name}" class="w-10 h-10 rounded-full object-cover mr-3">
                    <div class="flex-1 min-w-0">
                        <div class="flex justify-between items-center">
                            <span class="font-semibold truncate">${chat.name}</span>
                            <span class="text-xs text-gray-500">${chat.date}</span>
                        </div>
                        <p class="text-sm text-gray-600 truncate">${chat.message}</p>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Chat Main Content -->
    <div class="flex-1 flex flex-col items-center justify-center bg-gray-100 p-4">
        <div class="text-center max-w-md">
            <img src="https://placehold.co/150x150" alt="Chat illustration" class="mx-auto mb-6 w-32 h-32">
            <h3 class="text-xl font-semibold text-gray-800 mb-2">Chào mừng bạn đến với Chat</h3>
            <p class="text-gray-600">Chọn một cuộc trò chuyện để bắt đầu</p>
        </div>
    </div>
</div>

<script>
    // Chat toggle logic
    document.getElementById('chatButton').addEventListener('click', () => {
        document.getElementById('chatContainer').classList.toggle('show');
    });

    document.getElementById('closeChat').addEventListener('click', () => {
        document.getElementById('chatContainer').classList.remove('show');
    });
</script>