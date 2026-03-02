import {apiClient} from './api';
import {Tag} from '../types/tag';

export const tagsService = {
    async getTags(page = 1, limit = 50) {
        const response = await apiClient.get<{ items: Tag[] }>('/tags', {
            params: {page, limit}
        });
        return response.data;
    },

    async getTag(id: string) {
        const response = await apiClient.get<Tag>(`/tags/${id}`);
        return response.data;
    }
};