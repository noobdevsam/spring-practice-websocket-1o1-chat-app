'use strict';

/**
 * DOM Elements - Cached for better performance
 * @type {Object}
 */
const DOM = {
    usernamePage: document.querySelector('#username-page'), // Page for entering username
    chatPage: document.querySelector('#chat-page'), // Chat interface page
    usernameForm: document.querySelector('#usernameForm'), // Form for submitting username
    messageForm: document.querySelector('#messageForm'), // Form for sending messages
    messageInput: document.querySelector('#message'), // Input field for message content
    connectingElement: document.querySelector('.connecting'), // Element showing connection status
    chatArea: document.querySelector('#chat-messages'), // Area displaying chat messages
    logout: document.querySelector('#logout'), // Logout button
    connectedUsersList: document.getElementById('connectedUsers'), // List of connected users
    connectedUserFullname: document.querySelector('#connected-user-fullname') // Full name of the connected user
};

/**
 * Application State
 * @type {Object}
 */
const state = {
    stompClient: null, // WebSocket client instance
    nickname: null, // User's nickname
    fullname: null, // User's full name
    selectedUserId: null, // ID of the currently selected user
    connectedUsers: [] // List of connected users
};

/**
 * Configuration constants
 * @type {Object}
 */
const CONFIG = {
    USER_ICON_PATH: '../img/user_icon.png', // Path to user icon image
    ENDPOINTS: {
        WS: '/ws', // WebSocket endpoint
        USERS: '/users', // Endpoint for fetching users
        MESSAGES: (sender, recipient) => `/messages/${sender}/${recipient}`, // Endpoint for fetching messages
        USER_ADD: '/app/user/add', // Endpoint for adding a user
        USER_DISCONNECT: '/app/user/disconnect', // Endpoint for disconnecting a user
        CHAT: '/app/chat' // Endpoint for sending chat messages
    },
    SUBSCRIPTIONS: {
        MESSAGES: (nickname) => `/user/${nickname}/queue/messages`, // Subscription for private messages
        PUBLIC: '/topic/public' // Subscription for public updates
    }
};

/**
 * Utility functions for DOM manipulation and other operations
 * @type {Object}
 */
const utils = {
    /**
     * Creates a DOM element with optional class and text content
     * @param {string} tag - HTML tag name
     * @param {string} [className] - CSS class name
     * @param {string} [textContent] - Text content
     * @returns {HTMLElement} - Created element
     */
    createElement: (tag, className, textContent = '') => {
        const element = document.createElement(tag);
        if (className) element.className = className;
        if (textContent) element.textContent = textContent;
        return element;
    },

    /**
     * Toggles a CSS class on an element
     * @param {HTMLElement} element - Target element
     * @param {string} className - CSS class name
     * @param {boolean} [force] - Force toggle state
     */
    toggleClass: (element, className, force) => {
        if (force !== undefined) {
            element.classList.toggle(className, force);
        } else {
            element.classList.toggle(className);
        }
    },

    /**
     * Scrolls an element to its bottom
     * @param {HTMLElement} element - Target element
     */
    scrollToBottom: (element) => {
        element.scrollTop = element.scrollHeight;
    },

    /**
     * Clears the content of an element
     * @param {HTMLElement} element - Target element
     */
    clearElement: (element) => {
        element.innerHTML = '';
    }
};

/**
 * API functions for interacting with the server
 * @type {Object}
 */
const api = {
    /**
     * Fetches the list of connected users
     * @returns {Promise<Array>} - List of users
     */
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

    /**
     * Fetches messages between two users
     * @param {string} senderId - Sender's ID
     * @param {string} recipientId - Recipient's ID
     * @returns {Promise<Array>} - List of messages
     */
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

/**
 * WebSocket functions for real-time communication
 * @type {Object}
 */
const websocket = {
    /**
     * Establishes a WebSocket connection
     */
    connect: () => {
        const socket = new SockJS(CONFIG.ENDPOINTS.WS);
        state.stompClient = Stomp.over(socket);
        state.stompClient.connect({}, websocket.onConnected, websocket.onError);
    },

    /**
     * Handles successful WebSocket connection
     */
    onConnected: () => {
        try {
            state.stompClient.subscribe(
                CONFIG.SUBSCRIPTIONS.MESSAGES(state.nickname),
                messageHandler.onMessageReceived
            );
            state.stompClient.subscribe(
                CONFIG.SUBSCRIPTIONS.PUBLIC,
                messageHandler.onUserStatusUpdate
            );
            websocket.sendUserStatus('ONLINE');
            DOM.connectedUserFullname.textContent = state.fullname;
            ui.findAndDisplayConnectedUsers();
        } catch (error) {
            console.error('Connection setup failed:', error);
            websocket.onError();
        }
    },

    /**
     * Handles WebSocket connection errors
     */
    onError: () => {
        if (DOM.connectingElement) {
            DOM.connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
            DOM.connectingElement.style.color = 'red';
        }
    },

    /**
     * Sends a chat message via WebSocket
     * @param {Object} chatMessage - Chat message object
     */
    sendMessage: (chatMessage) => {
        if (state.stompClient && state.stompClient.connected) {
            state.stompClient.send(CONFIG.ENDPOINTS.CHAT, {}, JSON.stringify(chatMessage));
        }
    },

    /**
     * Sends user status (e.g., ONLINE/OFFLINE) via WebSocket
     * @param {string} status - User status
     */
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

/**
 * UI functions for managing the user interface
 * @type {Object}
 */
const ui = {
    /**
     * Displays the chat page and hides the username page
     */
    showChatPage: () => {
        utils.toggleClass(DOM.usernamePage, 'hidden', true);
        utils.toggleClass(DOM.chatPage, 'hidden', false);
    },

    /**
     * Fetches and displays the list of connected users
     */
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

    /**
     * Appends a user element to the connected users list
     * @param {Object} user - User object
     */
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

    /**
     * Appends a separator element to the connected users list
     */
    appendSeparator: () => {
        const separator = utils.createElement('li', 'separator');
        DOM.connectedUsersList.appendChild(separator);
    },

    /**
     * Handles user click events in the connected users list
     * @param {Event} event - Click event
     */
    handleUserClick: (event) => {
        document.querySelectorAll('.user-item').forEach(item => {
            utils.toggleClass(item, 'active', false);
        });
        utils.toggleClass(DOM.messageForm, 'hidden', false);
        const clickedUser = event.currentTarget;
        utils.toggleClass(clickedUser, 'active', true);
        state.selectedUserId = clickedUser.id;
        ui.fetchAndDisplayUserChat();
        const nbrMsg = clickedUser.querySelector('.nbr-msg');
        utils.toggleClass(nbrMsg, 'hidden', true);
        nbrMsg.textContent = '0';
    },

    /**
     * Displays a chat message in the chat area
     * @param {string} senderId - Sender's ID
     * @param {string} content - Message content
     */
    displayMessage: (senderId, content) => {
        const messageContainer = utils.createElement('div', 'message');
        const messageClass = senderId === state.nickname ? 'sender' : 'receiver';
        messageContainer.classList.add(messageClass);
        const message = utils.createElement('p', '', content);
        messageContainer.appendChild(message);
        DOM.chatArea.appendChild(messageContainer);
    },

    /**
     * Fetches and displays the chat history with the selected user
     */
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

/**
 * Message handler functions for processing incoming WebSocket messages
 * @type {Object}
 */
const messageHandler = {
    /**
     * Handles incoming private messages
     * @param {Object} payload - WebSocket message payload
     */
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

    /**
     * Handles user status updates (e.g., ONLINE/OFFLINE)
     * @param {Object} payload - WebSocket message payload
     */
    onUserStatusUpdate: async (payload) => {
        try {
            const userStatus = JSON.parse(payload.body);
            console.log('User status updated', userStatus);
            await ui.findAndDisplayConnectedUsers();
            if (userStatus.status === 'OFFLINE' && state.selectedUserId === userStatus.nickName) {
                state.selectedUserId = null;
                utils.toggleClass(DOM.messageForm, 'hidden', true);
                utils.clearElement(DOM.chatArea);
            }
            messageHandler.updateActiveUser();
        } catch (error) {
            console.error('Error processing user status update:', error);
        }
    },

    /**
     * Updates the active user selection in the UI
     */
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

    /**
     * Updates the notification counter for a user
     * @param {string} senderId - Sender's ID
     */
    updateNotificationCounter: (senderId) => {
        const notifiedUser = document.querySelector(`#${senderId}`);
        if (notifiedUser && !notifiedUser.classList.contains('active')) {
            const nbrMsg = notifiedUser.querySelector('.nbr-msg');
            utils.toggleClass(nbrMsg, 'hidden', false);
            nbrMsg.textContent = '';
        }
    }
};

/**
 * Event handler functions for user interactions
 * @type {Object}
 */
const eventHandlers = {
    /**
     * Handles the connection event when the user submits their username
     * @param {Event} event - Submit event
     */
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

    /**
     * Handles the send message event
     * @param {Event} event - Submit event
     */
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

    /**
     * Handles the logout event
     * @param {Event} [event] - Click or beforeunload event
     */
    logout: (event) => {
        websocket.sendUserStatus('OFFLINE');
        if (!event || event.type !== 'beforeunload') {
            window.location.reload();
        }
    }
};

/**
 * Initializes event listeners for the application
 */
const initializeEventListeners = () => {
    DOM.usernameForm.addEventListener('submit', eventHandlers.connect);
    DOM.messageForm.addEventListener('submit', eventHandlers.sendMessage);
    DOM.logout.addEventListener('click', eventHandlers.logout);
    window.addEventListener('beforeunload', eventHandlers.logout);
};

// Initialize the application
initializeEventListeners();
