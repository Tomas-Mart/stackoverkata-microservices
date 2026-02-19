import { apiClient } from './api';

export interface User {
    id: string;
    username: string;
    email: string;
    reputation: number;
    createdAt: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    username: string;
    email: string;
    password: string;
}

export const authService = {
    async login(data: LoginRequest) {
        const response = await apiClient.post('/auth/login', data);
        const { token, user } = response.data;
        localStorage.setItem('token', token);
        return user;
    },

    async register(data: RegisterRequest) {
        const response = await apiClient.post('/auth/register', data);
        const { token, user } = response.data;
        localStorage.setItem('token', token);
        return user;
    },

    async getCurrentUser() {
        const response = await apiClient.get<User>('/auth/me');
        return response.data;
    },

    logout() {
        localStorage.removeItem('token');
        window.location.href = '/login';
    },
};