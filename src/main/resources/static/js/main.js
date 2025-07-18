'use strict';

// DOM Elements - Cache for better performance
const DOM = {
    usernamePage: document.querySelector('#username-page'),
    chatPage: document.querySelector('#chat-page'),
    usernameForm: document.querySelector('#usernameForm'),
    messageForm: document.querySelector('#messageForm'),
    messageInput: document.querySelector('#message'),
    connectingElement: document.querySelector('.connecting'),
    chatArea: document.querySelector('#chat-messages'),
    logout: document.querySelector('#logout'),
    connectedUsersList: document.getElementById('connectedUsers'),
    connectedUserFullname: document.querySelector('#connected-user-fullname')
};

// Application State
const state = {
    stompClient: null,
    nickname: null,
    fullname: null,
    selectedUserId: null,
    connectedUsers: []
};

// Configuration
const CONFIG = {
    USER_ICON_PATH: '../img/user_icon.png',
    ENDPOINTS: {
        WS: '/ws',
        USERS: '/users',
        MESSAGES: (sender, recipient) => `/messages/${sender}/${recipient}`,
        USER_ADD: '/app/user/add',
        USER_DISCONNECT: '/app/user/disconnect',
        CHAT: '/app/chat'
    },
    SUBSCRIPTIONS: {
        MESSAGES: (nickname) => `/user/${nickname}/queue/messages`,
        PUBLIC: '/user/public'
    }
};

// Utility Functions
const utils = {
    createElement: (tag, className, textContent = '') => {
        const element = document.createElement(tag);
        if (className) element.className = className;
        if (textContent) element.textContent = textContent;
        return element;
    },

    toggleClass: (element, className, force) => {
        if (force !== undefined) {
            element.classList.toggle(className, force);
        } else {
            element.classList.toggle(className);
        }
    },

    scrollToBottom: (element) => {
        element.scrollTop = element.scrollHeight;
    },

    clearElement: (element) => {
        element.innerHTML = '';
    }
};

// API Functions
const api = {
    fetchUsers: async () => {
        try {
            const response = await fetch(CONFIG.ENDPOINTS.USERS);
            if (!response.ok) throw new Error('Failed to fetch users');
            return await response.json();
        } catch (error) {
            console.error('Error fetching users:', error);
            return [];
        }
    },

    fetchMessages: async (senderId, recipientId) => {
        try {
            const response = await fetch(CONFIG.ENDPOINTS.MESSAGES(senderId, recipientId));
            if (!response.ok) throw new Error('Failed to fetch messages');
            return await response.json();
        } catch (error) {
            console.error('Error fetching messages:', error);
            return [];
        }
    }
};

// WebSocket Functions
const websocket = {
    connect: () => {
        const socket = new SockJS(CONFIG.ENDPOINTS.WS);
        state.stompClient = Stomp.over(socket);
        state.stompClient.connect({}, websocket.onConnected, websocket.onError);
    },

    onConnected: () => {
        try {
            // Subscribe to message queues
            state.stompClient.subscribe(
                CONFIG.SUBSCRIPTIONS.MESSAGES(state.nickname),
                messageHandler.onMessageReceived
            );
            state.stompClient.subscribe(
                CONFIG.SUBSCRIPTIONS.PUBLIC,
                messageHandler.onMessageReceived
            );

            // Register the connected user
            websocket.sendUserStatus('ONLINE');

            DOM.connectedUserFullname.textContent = state.fullname;
            ui.findAndDisplayConnectedUsers();
        } catch (error) {
            console.error('Connection setup failed:', error);
            websocket.onError();
        }
    },

    onError: () => {
        if (DOM.connectingElement) {
            DOM.connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
            DOM.connectingElement.style.color = 'red';
        }
    },

    sendMessage: (chatMessage) => {
        if (state.stompClient && state.stompClient.connected) {
            state.stompClient.send(CONFIG.ENDPOINTS.CHAT, {}, JSON.stringify(chatMessage));
        }
    },

    sendUserStatus: (status) => {
        if (state.stompClient && state.stompClient.connected) {
            const userStatus = {
                nickName: state.nickname,
                fullName: state.fullname,
                status
            };
            const endpoint = status === 'ONLINE' ? CONFIG.ENDPOINTS.USER_ADD : CONFIG.ENDPOINTS.USER_DISCONNECT;
            state.stompClient.send(endpoint, {}, JSON.stringify(userStatus));
        }
    }
};

// UI Functions
const ui = {
    showChatPage: () => {
        utils.toggleClass(DOM.usernamePage, 'hidden', true);
        utils.toggleClass(DOM.chatPage, 'hidden', false);
    },

    findAndDisplayConnectedUsers: async () => {
        const users = await api.fetchUsers();
        state.connectedUsers = users.filter(user => user.nickName !== state.nickname);

        utils.clearElement(DOM.connectedUsersList);

        state.connectedUsers.forEach((user, index) => {
            ui.appendUserElement(user);
            if (index < state.connectedUsers.length - 1) {
                ui.appendSeparator();
            }
        });
    },

    appendUserElement: (user) => {
        const listItem = utils.createElement('li', 'user-item');
        listItem.id = user.nickName;

        const userImage = utils.createElement('img');
        userImage.src = CONFIG.USER_ICON_PATH;
        userImage.alt = user.fullName;

        const usernameSpan = utils.createElement('span', '', user.fullName);
        const receivedMsgs = utils.createElement('span', 'nbr-msg hidden', '0');

        listItem.append(userImage, usernameSpan, receivedMsgs);
        listItem.addEventListener('click', ui.handleUserClick);

        DOM.connectedUsersList.appendChild(listItem);
    },

    appendSeparator: () => {
        const separator = utils.createElement('li', 'separator');
        DOM.connectedUsersList.appendChild(separator);
    },

    handleUserClick: (event) => {
        // Remove active class from all users
        document.querySelectorAll('.user-item').forEach(item => {
            utils.toggleClass(item, 'active', false);
        });

        // Show message form and set active user
        utils.toggleClass(DOM.messageForm, 'hidden', false);

        const clickedUser = event.currentTarget;
        utils.toggleClass(clickedUser, 'active', true);

        state.selectedUserId = clickedUser.id;
        ui.fetchAndDisplayUserChat();

        // Reset message counter
        const nbrMsg = clickedUser.querySelector('.nbr-msg');
        utils.toggleClass(nbrMsg, 'hidden', true);
        nbrMsg.textContent = '0';
    },

    displayMessage: (senderId, content) => {
        const messageContainer = utils.createElement('div', 'message');
        const messageClass = senderId === state.nickname ? 'sender' : 'receiver';
        messageContainer.classList.add(messageClass);

        const message = utils.createElement('p', '', content);
        messageContainer.appendChild(message);
        DOM.chatArea.appendChild(messageContainer);
    },

    fetchAndDisplayUserChat: async () => {
        if (!state.selectedUserId) return;

        const messages = await api.fetchMessages(state.nickname, state.selectedUserId);
        utils.clearElement(DOM.chatArea);

        messages.forEach(chat => {
            ui.displayMessage(chat.senderId, chat.content);
        });

        utils.scrollToBottom(DOM.chatArea);
    }
};

// Message Handler
const messageHandler = {
    onMessageReceived: async (payload) => {
        try {
            await ui.findAndDisplayConnectedUsers();
            console.log('Message received', payload);

            const message = JSON.parse(payload.body);

            if (state.selectedUserId === message.senderId) {
                ui.displayMessage(message.senderId, message.content);
                utils.scrollToBottom(DOM.chatArea);
            }

            messageHandler.updateActiveUser();
            messageHandler.updateNotificationCounter(message.senderId);
        } catch (error) {
            console.error('Error processing received message:', error);
        }
    },

    updateActiveUser: () => {
        if (state.selectedUserId) {
            const selectedUser = document.querySelector(`#${state.selectedUserId}`);
            if (selectedUser) {
                utils.toggleClass(selectedUser, 'active', true);
            }
        } else {
            utils.toggleClass(DOM.messageForm, 'hidden', true);
        }
    },

    updateNotificationCounter: (senderId) => {
        const notifiedUser = document.querySelector(`#${senderId}`);
        if (notifiedUser && !notifiedUser.classList.contains('active')) {
            const nbrMsg = notifiedUser.querySelector('.nbr-msg');
            utils.toggleClass(nbrMsg, 'hidden', false);
            nbrMsg.textContent = '';
        }
    }
};

// Event Handlers
const eventHandlers = {
    connect: (event) => {
        event.preventDefault();

        const nicknameInput = document.querySelector('#nickname');
        const fullnameInput = document.querySelector('#fullname');

        state.nickname = nicknameInput.value.trim();
        state.fullname = fullnameInput.value.trim();

        if (state.nickname && state.fullname) {
            ui.showChatPage();
            websocket.connect();
        }
    },

    sendMessage: (event) => {
        event.preventDefault();

        const messageContent = DOM.messageInput.value.trim();
        if (!messageContent || !state.stompClient || !state.selectedUserId) return;

        const chatMessage = {
            senderId: state.nickname,
            recipientId: state.selectedUserId,
            content: messageContent,
            timestamp: new Date()
        };

        websocket.sendMessage(chatMessage);
        ui.displayMessage(state.nickname, messageContent);
        DOM.messageInput.value = '';
        utils.scrollToBottom(DOM.chatArea);
    },

    logout: () => {
        websocket.sendUserStatus('OFFLINE');
        window.location.reload();
    }
};

// Initialize Event Listeners
const initializeEventListeners = () => {
    DOM.usernameForm.addEventListener('submit', eventHandlers.connect);
    DOM.messageForm.addEventListener('submit', eventHandlers.sendMessage);
    DOM.logout.addEventListener('click', eventHandlers.logout);
    window.addEventListener('beforeunload', eventHandlers.logout);
};

// Initialize the application
initializeEventListeners();
