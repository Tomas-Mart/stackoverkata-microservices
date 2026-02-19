import {Routes, Route, Navigate} from 'react-router-dom';
import {Layout} from './components/layout/Layout';
import {HomePage} from './pages/HomePage';
import {QuestionsPage} from './pages/QuestionsPage';
import {QuestionPage} from './pages/QuestionPage';
import {AskPage} from './pages/AskPage';
import {LoginPage} from './pages/LoginPage';
import {RegisterPage} from './pages/RegisterPage';
import {UsersPage} from './pages/UsersPage';
import {ProfilePage} from './pages/ProfilePage';

const PrivateRoute = ({children}: { children: JSX.Element }) => {
    const token = localStorage.getItem('token');
    return token ? children : <Navigate to="/login"/>;
};

export function AppRoutes() {
    return (
        <Routes>
            <Route path="/" element={<Layout/>}>
                <Route index element={<HomePage/>}/>
                <Route path="questions" element={<QuestionsPage/>}/>
                <Route path="questions/:id" element={<QuestionPage/>}/>
                <Route
                    path="ask"
                    element={
                        <PrivateRoute>
                            <AskPage/>
                        </PrivateRoute>
                    }
                />
                <Route path="users" element={<UsersPage/>}/>
                <Route path="users/:id" element={<ProfilePage/>}/>
            </Route>
            <Route path="/login" element={<LoginPage/>}/>
            <Route path="/register" element={<RegisterPage/>}/>
        </Routes>
    );
}