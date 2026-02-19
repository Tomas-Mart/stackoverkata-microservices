import React from 'react';
import {Box, Paper, Typography, Avatar, IconButton, Chip} from '@mui/material';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import ThumbDownIcon from '@mui/icons-material/ThumbDown';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import {useQuery, useMutation, useQueryClient} from 'react-query';
import {questionsService} from '../../services/questions';
import {formatDistanceToNow} from 'date-fns';
import {ru} from 'date-fns/locale';

interface AnswerListProps {
    questionId: string;
}

export function AnswerList({questionId}: AnswerListProps) {
    const queryClient = useQueryClient();

    const {data: answers, isLoading} = useQuery(
        ['answers', questionId],
        () => questionsService.getAnswers(questionId)
    );

    const voteMutation = useMutation(
        ({answerId, vote}: { answerId: string; vote: 1 | -1 }) =>
            questionsService.voteAnswer(answerId, vote),
        {
            onSuccess: () => {
                queryClient.invalidateQueries(['answers', questionId]);
            },
        }
    );

    const acceptMutation = useMutation(
        (answerId: string) => questionsService.acceptAnswer(answerId),
        {
            onSuccess: () => {
                queryClient.invalidateQueries(['answers', questionId]);
            },
        }
    );

    if (isLoading) {
        return <Typography>Загрузка ответов...</Typography>;
    }

    if (!answers || answers.length === 0) {
        return (
            <Paper sx={{p: 3, textAlign: 'center', bgcolor: '#f8f9f9'}}>
                <Typography color="text.secondary">
                    Пока нет ответов. Будьте первым!
                </Typography>
            </Paper>
        );
    }

    return (
        <Box>
            {answers.map((answer) => (
                <Paper
                    key={answer.id}
                    sx={{
                        p: 3,
                        mb: 2,
                        border: answer.isAccepted ? '2px solid' : 'none',
                        borderColor: 'success.main',
                        position: 'relative',
                    }}
                >
                    {answer.isAccepted && (
                        <Chip
                            icon={<CheckCircleIcon/>}
                            label="Принятый ответ"
                            color="success"
                            size="small"
                            sx={{position: 'absolute', top: 8, right: 8}}
                        />
                    )}

                    <Typography variant="body1" paragraph>
                        {answer.body}
                    </Typography>

                    <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                        <Box sx={{display: 'flex', alignItems: 'center', gap: 2}}>
                            <Box sx={{display: 'flex', alignItems: 'center'}}>
                                <IconButton
                                    size="small"
                                    onClick={() => voteMutation.mutate({answerId: answer.id, vote: 1})}
                                >
                                    <ThumbUpIcon fontSize="small"/>
                                </IconButton>
                                <Typography variant="body2">{answer.score || 0}</Typography>
                                <IconButton
                                    size="small"
                                    onClick={() => voteMutation.mutate({answerId: answer.id, vote: -1})}
                                >
                                    <ThumbDownIcon fontSize="small"/>
                                </IconButton>
                            </Box>

                            <Button
                                size="small"
                                color="success"
                                onClick={() => acceptMutation.mutate(answer.id)}
                                disabled={answer.isAccepted}
                            >
                                Принять ответ
                            </Button>
                        </Box>

                        <Box sx={{display: 'flex', alignItems: 'center', gap: 1}}>
                            <Avatar src={answer.author?.linkPhoto} sx={{width: 24, height: 24}}>
                                {answer.author?.username?.[0] || 'U'}
                            </Avatar>
                            <Typography variant="body2" color="primary">
                                {answer.author?.username || 'Пользователь'}
                            </Typography>
                            <Typography variant="caption" color="text.secondary">
                                {formatDistanceToNow(new Date(answer.createdAt), {
                                    addSuffix: true,
                                    locale: ru,
                                })}
                            </Typography>
                        </Box>
                    </Box>
                </Paper>
            ))}
        </Box>
    );
}