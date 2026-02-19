import { apiClient } from './api';

export interface Question {
    id: string;
    title: string;
    body: string;
    tags: string[];
    author: {
        id: string;
        username: string;
        reputation: number;
    };
    score: number;
    views: number;
    answerCount: number;
    createdAt: string;
    updatedAt: string;
}

export interface Answer {
    id: string;
    body: string;
    author: {
        id: string;
        username: string;
        reputation: number;
    };
    score: number;
    isAccepted: boolean;
    createdAt: string;
}

export const questionsService = {
    async getQuestions(page = 1, limit = 15) {
        const response = await apiClient.get('/questions', {
            params: { page, limit },
        });
        return response.data;
    },

    async getQuestion(id: string) {
        const response = await apiClient.get<Question>(`/questions/${id}`);
        return response.data;
    },

    async createQuestion(data: { title: string; body: string; tags: string[] }) {
        const response = await apiClient.post('/questions', data);
        return response.data;
    },

    async updateQuestion(id: string, data: Partial<Question>) {
        const response = await apiClient.put(`/questions/${id}`, data);
        return response.data;
    },

    async deleteQuestion(id: string) {
        await apiClient.delete(`/questions/${id}`);
    },

    async voteQuestion(id: string, vote: 1 | -1) {
        const response = await apiClient.post(`/questions/${id}/vote`, { vote });
        return response.data;
    },

    async getAnswers(questionId: string) {
        const response = await apiClient.get<Answer[]>(`/questions/${questionId}/answers`);
        return response.data;
    },

    async createAnswer(questionId: string, body: string) {
        const response = await apiClient.post(`/questions/${questionId}/answers`, { body });
        return response.data;
    },

    async voteAnswer(answerId: string, vote: 1 | -1) {
        const response = await apiClient.post(`/answers/${answerId}/vote`, { vote });
        return response.data;
    },

    async acceptAnswer(answerId: string) {
        const response = await apiClient.post(`/answers/${answerId}/accept`);
        return response.data;
    },
};