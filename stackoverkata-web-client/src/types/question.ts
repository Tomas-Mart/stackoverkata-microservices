import {User} from './user';
import {Tag} from './tag';

export interface Question {
    id: number;
    title: string;
    description: string;
    user: User;
    tags: Tag[];
    votes: number;
    views: number;
    answersCount: number;
    creationDate: string;
    updateDate: string;
    isDeleted: boolean;
    isAnswered: boolean;
}

export interface CommentQuestion {
    id: number;
    questionId: number;
    userId: number;
    text: string;
    creationDate: string;
    updateDate: string;
    isDeleted: boolean;
}

export interface VoteQuestion {
    id: number;
    userId: number;
    questionId: number;
    voteType: VoteTypeQuestion;
    voteDate: string;
}

export enum VoteTypeQuestion {
    UP_VOTE = 'UP_VOTE',
    DOWN_VOTE = 'DOWN_VOTE'
}

export interface TrackedTag {
    id: number;
    userId: number;
    tagId: number;
    trackedDate: string;
}

export interface IgnoredTag {
    id: number;
    userId: number;
    tagId: number;
    ignoredDate: string;
}

export interface RelatedTag {
    id: number;
    questionId: number;
    tagId: number;
}

export interface QuestionViewed {
    id: number;
    userId: number;
    questionId: number;
    viewDate: string;
}

export interface Bookmark {
    id: number;
    userId: number;
    questionId: number;
    bookmarkDate: string;
}