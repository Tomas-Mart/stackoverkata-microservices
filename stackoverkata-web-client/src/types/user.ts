export interface User {
    id: number;
    accountId: number;
    username: string;
    email: string;
    fullName: string;
    linkPhoto: string;
    city: string;
    reputation: number;
    registrationDate: string;
    lastVisit: string;
    isEnabled: boolean;
    createdAt: string;
}

export interface UserBadge {
    id: number;
    userId: number;
    badgeId: number;
    awardedDate: string;
}

export interface UserFavoriteQuestion {
    id: number;
    userId: number;
    questionId: number;
    addedDate: string;
}

export enum RoleName {
    ROLE_USER = 'ROLE_USER',
    ROLE_ADMIN = 'ROLE_ADMIN',
    ROLE_MODERATOR = 'ROLE_MODERATOR'
}