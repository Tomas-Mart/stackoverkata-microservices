import React, {useState} from 'react';
import {Box, TextField, Button, Paper} from '@mui/material';
import {useMutation, useQueryClient} from 'react-query';
import {questionsService} from '../../services/questions';

interface AnswerFormProps {
    questionId: string;
}

export function AnswerForm({questionId}: AnswerFormProps) {
    const queryClient = useQueryClient();
    const [answer, setAnswer] = useState('');

    const mutation = useMutation(
        () => questionsService.createAnswer(questionId, answer),
        {
            onSuccess: () => {
                setAnswer('');
                queryClient.invalidateQueries(['answers', questionId]);
            },
        }
    );

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (answer.trim()) {
            mutation.mutate();
        }
    };

    return (
        <Paper sx={{p: 3, mt: 3}}>
            <form onSubmit={handleSubmit}>
                <TextField
                    fullWidth
                    multiline
                    rows={4}
                    value={answer}
                    onChange={(e) => setAnswer(e.target.value)}
                    placeholder="Напишите ваш ответ..."
                    variant="outlined"
                    sx={{mb: 2}}
                />
                <Button
                    type="submit"
                    variant="contained"
                    disabled={!answer.trim() || mutation.isLoading}
                >
                    {mutation.isLoading ? 'Отправка...' : 'Отправить ответ'}
                </Button>
            </form>
        </Paper>
    );
}