import {useState} from 'react';
import {Box, Typography, Button, Pagination} from '@mui/material';
import {useNavigate} from 'react-router-dom';
import {useQuery} from 'react-query';
import {questionsService} from '../services/questions';
import {QuestionList} from '../components/questions/QuestionList';

export function QuestionsPage() {
    const navigate = useNavigate();
    const [page, setPage] = useState(1);

    const {data, isLoading} = useQuery(
        ['questions', page],
        () => questionsService.getQuestions(page, 15)
    );

    return (
        <Box>
            <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3}}>
                <Typography variant="h4">Все вопросы</Typography>
                <Button
                    variant="contained"
                    onClick={() => navigate('/ask')}
                >
                    Задать вопрос
                </Button>
            </Box>

            <QuestionList questions={data?.items || []} loading={isLoading}/>

            {data?.totalPages > 1 && (
                <Box sx={{display: 'flex', justifyContent: 'center', mt: 4}}>
                    <Pagination
                        count={data.totalPages}
                        page={page}
                        onChange={(_, value) => setPage(value)}
                        color="primary"
                    />
                </Box>
            )}
        </Box>
    );
}