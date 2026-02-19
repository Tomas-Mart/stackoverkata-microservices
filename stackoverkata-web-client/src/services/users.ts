import {apiClient} from './api';
import {User} from '../types/user';

export interface UsersResponse {
    items: User[];
    total: number;
    page: number;
    totalPages: number;
}

export const usersService = {
    async getUsers(search?: string, page = 1, limit = 20) {
        const response = await apiClient.get<UsersResponse>('/users', {
            params: {search, page, limit}
        });
        return response.data;
    },

    async getUser(id: string) {
        const response = await apiClient.get<User>(`/users/${id}`);
        return response.data;
    },

    async updateUser(id: string, data: Partial<User>) {
        const response = await apiClient.put<User>(`/users/${id}`, data);
        return response.data;
    }
};