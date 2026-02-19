import React from 'react';
import {Question} from '../../types/question';
import {User} from '../../types/user';
import {Tag} from '../../types/tag';
import {Link} from 'react-router-dom';
import {Box, Typography, Chip, Avatar, Paper, Skeleton} from '@mui/material';
import {formatDistanceToNow} from 'date-fns';
import {ru} from 'date-fns/locale';

interface QuestionListProps {
    questions: Question[];
    loading: boolean;
}

export function QuestionList({questions, loading}: QuestionListProps) {
    if (loading) {
        return (
            <Box>
                {[1, 2, 3, 4, 5].map((i) => (
                    <Paper key={i} sx={{p: 2, mb: 2}}>
                        <Skeleton variant="text" height={30} width="80%"/>
                        <Skeleton variant="text" height={20} width="40%"/>
                        <Box sx={{mt: 1, display: 'flex', gap: 1}}>
                            <Skeleton variant="rectangular" width={60} height={24}/>
                            <Skeleton variant="rectangular" width={60} height={24}/>
                        </Box>
                    </Paper>
                ))}
            </Box>
        );
    }

    if (!questions || questions.length === 0) {
        return (
            <Paper sx={{p: 4, textAlign: 'center'}}>
                <Typography variant="h6" color="text.secondary">
                    Вопросов пока нет
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{mt: 1}}>
                    Будьте первым, кто задаст вопрос!
                </Typography>
            </Paper>
        );
    }

    return (
        <Box>
            {questions.map((question) => (
                <Paper
                    key={question.id}
                    sx={{
                        p: 2,
                        mb: 2,
                        '&:hover': {
                            boxShadow: 3,
                            transition: 'box-shadow 0.2s ease-in-out',
                        },
                    }}
                >
                    <Box sx={{display: 'flex', gap: 2}}>
                        {/* Статистика вопросов */}
                        <Box sx={{minWidth: 100, textAlign: 'center'}}>
                            <Typography variant="body2" color="text.secondary">
                                {question.votes || 0} голосов
                            </Typography>
                            <Typography
                                variant="body2"
                                color={question.answersCount > 0 ? 'success.main' : 'text.secondary'}
                                sx={{fontWeight: question.answersCount > 0 ? 'bold' : 'normal'}}
                            >
                                {question.answersCount || 0} ответов
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                {question.views || 0} просмотров
                            </Typography>
                        </Box>

                        {/* Основное содержание вопроса */}
                        <Box sx={{flex: 1}}>
                            <Link
                                to={`/questions/${question.id}`}
                                style={{textDecoration: 'none'}}
                            >
                                <Typography
                                    variant="h6"
                                    color="primary"
                                    sx={{
                                        '&:hover': {
                                            textDecoration: 'underline',
                                        },
                                    }}
                                >
                                    {question.title}
                                </Typography>
                            </Link>

                            <Typography
                                variant="body2"
                                color="text.secondary"
                                sx={{
                                    mt: 1,
                                    overflow: 'hidden',
                                    textOverflow: 'ellipsis',
                                    display: '-webkit-box',
                                    WebkitLineClamp: 2,
                                    WebkitBoxOrient: 'vertical',
                                }}
                            >
                                {question.description}
                            </Typography>

                            {/* Теги */}
                            {question.tags && question.tags.length > 0 && (
                                <Box sx={{mt: 2, display: 'flex', gap: 1, flexWrap: 'wrap'}}>
                                    {question.tags.map((tag) => (
                                        <Chip
                                            key={tag.id}
                                            label={tag.name}
                                            size="small"
                                            sx={{
                                                bgcolor: '#e1ecf4',
                                                color: '#39739d',
                                                '&:hover': {
                                                    bgcolor: '#d0e2f0',
                                                },
                                            }}
                                        />
                                    ))}
                                </Box>
                            )}

                            {/* Информация об авторе */}
                            {question.user && (
                                <Box
                                    sx={{
                                        mt: 2,
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'flex-end',
                                        gap: 1,
                                    }}
                                >
                                    <Avatar
                                        src={question.user.linkPhoto}
                                        sx={{width: 24, height: 24}}
                                    >
                                        {question.user.fullName?.[0] || 'U'}
                                    </Avatar>
                                    <Link
                                        to={`/users/${question.user.id}`}
                                        style={{textDecoration: 'none'}}
                                    >
                                        <Typography variant="body2" color="primary">
                                            {question.user.fullName || 'Пользователь'}
                                        </Typography>
                                    </Link>
                                    <Typography variant="caption" color="text.secondary">
                                        {formatDistanceToNow(new Date(question.creationDate), {
                                            addSuffix: true,
                                            locale: ru,
                                        })}
                                    </Typography>
                                </Box>
                            )}
                        </Box>
                    </Box>
                </Paper>
            ))}
        </Box>
    );
}