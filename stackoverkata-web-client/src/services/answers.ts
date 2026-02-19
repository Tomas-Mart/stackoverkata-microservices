import {apiClient} from './api';
import {Answer} from '../types/answer';

export const answersService = {
    async getAnswers(questionId: string) {
        const response = await apiClient.get<Answer[]>(`/questions/${questionId}/answers`);
        return response.data;
    },

    async createAnswer(questionId: string, body: string) {
        const response = await apiClient.post<Answer>(`/questions/${questionId}/answers`, {body});
        return response.data;
    },

    async voteAnswer(answerId: string, vote: 1 | -1) {
        const response = await apiClient.post(`/answers/${answerId}/vote`, {vote});
        return response.data;
    },

    async acceptAnswer(answerId: string) {
        const response = await apiClient.post(`/answers/${answerId}/accept`);
        return response.data;
    }
};