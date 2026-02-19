import {User} from './user';

export interface Answer {
    id: number;
    userId: number;
    questionId: number;
    htmlBody: string;
    user: User;
    votes: number;
    isAccepted: boolean;
    creationDate: string;
    updateDate: string;
    isDeleted: boolean;
    isDeletedByModerator: boolean;
}

export interface CommentAnswer {
    id: number;
    answerId: number;
    userId: number;
    text: string;
    creationDate: string;
    updateDate: string;
    isDeleted: boolean;
}

export interface VoteAnswer {
    id: number;
    userId: number;
    answerId: number;
    voteType: VoteTypeAnswer;
    voteDate: string;
}

export enum VoteTypeAnswer {
    UP_VOTE = 'UP_VOTE',
    DOWN_VOTE = 'DOWN_VOTE'
}