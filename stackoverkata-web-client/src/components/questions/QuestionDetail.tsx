import React, {useState} from 'react';
import {
    Box,
    Typography,
    Paper,
    Chip,
    Avatar,
    Button,
    Divider,
    TextField,
    IconButton,
} from '@mui/material';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import ThumbDownIcon from '@mui/icons-material/ThumbDown';
import BookmarkIcon from '@mui/icons-material/Bookmark';
import {useParams} from 'react-router-dom';
import {useQuery, useMutation, useQueryClient} from 'react-query';
import {questionsService} from '../../services/questions';
import {AnswerList} from '../answers/AnswerList';
import {formatDistanceToNow} from 'date-fns';
import {ru} from 'date-fns/locale';

export function QuestionDetail() {
    const {id} = useParams<{ id: string }>();
    const queryClient = useQueryClient();
    const [newAnswer, setNewAnswer] = useState('');

    const {data: question, isLoading} = useQuery(
        ['question', id],
        () => questionsService.getQuestion(id!),
        {enabled: !!id}
    );

    const voteMutation = useMutation(
        (vote: 1 | -1) => questionsService.voteQuestion(id!, vote),
        {
            onSuccess: () => {
                queryClient.invalidateQueries(['question', id]);
            },
        }
    );

    const answerMutation = useMutation(
        () => questionsService.createAnswer(id!, newAnswer),
        {
            onSuccess: () => {
                setNewAnswer('');
                queryClient.invalidateQueries(['answers', id]);
            },
        }
    );

    if (isLoading || !question) {
        return <Typography>Загрузка...</Typography>;
    }

    return (
        <Box>
            <Paper sx={{p: 3, mb: 3}}>
                <Typography variant="h4" gutterBottom>
                    {question.title}
                </Typography>

                <Box sx={{display: 'flex', gap: 1, mb: 2, flexWrap: 'wrap'}}>
                    {question.tags?.map((tag) => (
                        <Chip
                            key={tag.id}
                            label={tag.name}
                            size="small"
                            sx={{bgcolor: '#e1ecf4', color: '#39739d'}}
                        />
                    ))}
                </Box>

                <Typography variant="body1" paragraph>
                    {question.description}
                </Typography>

                <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mt: 2}}>
                    <Box sx={{display: 'flex', alignItems: 'center', gap: 2}}>
                        <Box sx={{display: 'flex', alignItems: 'center'}}>
                            <IconButton onClick={() => voteMutation.mutate(1)}>
                                <ThumbUpIcon/>
                            </IconButton>
                            <Typography>{question.votes || 0}</Typography>
                            <IconButton onClick={() => voteMutation.mutate(-1)}>
                                <ThumbDownIcon/>
                            </IconButton>
                        </Box>
                        <IconButton>
                            <BookmarkIcon/>
                        </IconButton>
                    </Box>

                    <Box sx={{display: 'flex', alignItems: 'center', gap: 1}}>
                        <Avatar src={question.user?.linkPhoto} sx={{width: 32, height: 32}}>
                            {question.user?.fullName?.[0] || 'U'}
                        </Avatar>
                        <Box>
                            <Typography variant="body2" color="primary">
                                {question.user?.fullName || 'Пользователь'}
                            </Typography>
                            <Typography variant="caption" color="text.secondary">
                                {formatDistanceToNow(new Date(question.creationDate), {
                                    addSuffix: true,
                                    locale: ru,
                                })}
                            </Typography>
                        </Box>
                    </Box>
                </Box>
            </Paper>

            <Typography variant="h5" gutterBottom>
                Ответы
            </Typography>

            <AnswerList questionId={id!}/>

            <Paper sx={{p: 3, mt: 3}}>
                <Typography variant="h6" gutterBottom>
                    Ваш ответ
                </Typography>
                <TextField
                    fullWidth
                    multiline
                    rows={4}
                    value={newAnswer}
                    onChange={(e) => setNewAnswer(e.target.value)}
                    placeholder="Напишите ваш ответ..."
                    variant="outlined"
                    sx={{mb: 2}}
                />
                <Button
                    variant="contained"
                    onClick={() => answerMutation.mutate()}
                    disabled={!newAnswer.trim()}
                >
                    Отправить
                </Button>
            </Paper>
        </Box>
    );
}