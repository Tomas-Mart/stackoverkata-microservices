import {User} from './user';

export enum ReputationType {
    QUESTION_UPVOTE = 'QUESTION_UPVOTE',
    QUESTION_DOWNVOTE = 'QUESTION_DOWNVOTE',
    ANSWER_UPVOTE = 'ANSWER_UPVOTE',
    ANSWER_DOWNVOTE = 'ANSWER_DOWNVOTE',
    ANSWER_ACCEPT = 'ANSWER_ACCEPT',
    ANSWER_ACCEPT_CANCEL = 'ANSWER_ACCEPT_CANCEL'
}

export interface Reputation {
    id: number;
    userId: number;
    user: User;
    count: number;
    type: ReputationType;
    questionId: number;
    answerId: number;
    creationDate: string;
    description: string;
}

export interface CombinedNotNullQuestionOrAnswer {
    questionId: number;
    answerId: number;
}