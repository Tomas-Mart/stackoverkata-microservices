export interface Tag {
    id: number;
    name: string;
    description: string;
    questionsCount: number;
    creationDate: string;
    isDeleted: boolean;
}

export interface TagResponse {
    id: number;
    name: string;
    description: string;
    questionsCount: number;
}