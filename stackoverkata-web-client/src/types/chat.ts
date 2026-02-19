import {User} from './user';

export enum ChatType {
    SINGLE = 'SINGLE',
    GROUP = 'GROUP'
}

export interface Chat {
    id: number;
    chatType: ChatType;
    creationDate: string;
}

export interface SingleChat extends Chat {
    firstUser: User;
    secondUser: User;
}

export interface GroupChat extends Chat {
    name: string;
    users: User[];
}

export interface Message {
    id: number;
    chatId: number;
    userId: number;
    text: string;
    creationDate: string;
    isDeleted: boolean;
}