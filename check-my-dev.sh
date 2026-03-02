#!/bin/bash

echo "========================================"
echo "🔍 ПРОВЕРКА ТОЛЬКО РАЗРАБОТАННЫХ ПАПОК"
echo "========================================"

ROOT_PATH="/mnt/c/Users/Ami/IdeaProjects/stackoverkata-microservices/stackoverkata-web-client"

if [ ! -d "$ROOT_PATH" ]; then
    echo "❌ Папка не найдена: $ROOT_PATH"
    exit 1
fi

echo "✅ Корневая папка найдена"

# Функция проверки папки
check_folder() {
    if [ -d "$1" ]; then
        echo "  ✅ $2"
    else
        echo "  ❌ $2"
    fi
}

# Функция проверки файла
check_file() {
    if [ -f "$1" ]; then
        echo "    ✅ $2"
    else
        echo "    ❌ $2"
    fi
}

echo -e "\n📂 ПРОВЕРКА ПАПОК (только созданные вручную):"
echo "========================================"

echo -e "\n📄 Корневые файлы разработки:"
check_file "$ROOT_PATH/package.json" "package.json"
check_file "$ROOT_PATH/vite.config.ts" "vite.config.ts"
check_file "$ROOT_PATH/index.html" "index.html"
check_file "$ROOT_PATH/tsconfig.json" "tsconfig.json"
check_file "$ROOT_PATH/.env" ".env"

echo -e "\n📂 Папка src:"
check_folder "$ROOT_PATH/src" "src"

echo -e "\n📂 Папки src (ручная разработка):"
check_folder "$ROOT_PATH/src/types" "types"
check_folder "$ROOT_PATH/src/services" "services"
check_folder "$ROOT_PATH/src/components" "components"
check_folder "$ROOT_PATH/src/pages" "pages"
check_folder "$ROOT_PATH/src/styles" "styles"
check_folder "$ROOT_PATH/src/components/layout" "components/layout"
check_folder "$ROOT_PATH/src/components/questions" "components/questions"
check_folder "$ROOT_PATH/src/components/answers" "components/answers"
check_folder "$ROOT_PATH/src/components/tags" "components/tags"
check_folder "$ROOT_PATH/src/components/users" "components/users"

echo -e "\n📄 Файлы src (ручная разработка):"
check_file "$ROOT_PATH/src/App.tsx" "App.tsx"
check_file "$ROOT_PATH/src/main.tsx" "main.tsx"
check_file "$ROOT_PATH/src/routes.tsx" "routes.tsx"

echo -e "\n📄 Файлы types:"
check_file "$ROOT_PATH/src/types/user.ts" "user.ts"
check_file "$ROOT_PATH/src/types/question.ts" "question.ts"
check_file "$ROOT_PATH/src/types/answer.ts" "answer.ts"
check_file "$ROOT_PATH/src/types/tag.ts" "tag.ts"
check_file "$ROOT_PATH/src/types/chat.ts" "chat.ts"
check_file "$ROOT_PATH/src/types/reputation.ts" "reputation.ts"

echo -e "\n📄 Файлы services:"
check_file "$ROOT_PATH/src/services/api.ts" "api.ts"
check_file "$ROOT_PATH/src/services/auth.ts" "auth.ts"
check_file "$ROOT_PATH/src/services/questions.ts" "questions.ts"
check_file "$ROOT_PATH/src/services/answers.ts" "answers.ts"
check_file "$ROOT_PATH/src/services/tags.ts" "tags.ts"
check_file "$ROOT_PATH/src/services/users.ts" "users.ts"

echo -e "\n📄 Файлы layout:"
check_file "$ROOT_PATH/src/components/layout/Layout.tsx" "Layout.tsx"
check_file "$ROOT_PATH/src/components/layout/Header.tsx" "Header.tsx"
check_file "$ROOT_PATH/src/components/layout/Sidebar.tsx" "Sidebar.tsx"

echo -e "\n📄 Файлы questions:"
check_file "$ROOT_PATH/src/components/questions/QuestionList.tsx" "QuestionList.tsx"
check_file "$ROOT_PATH/src/components/questions/QuestionDetail.tsx" "QuestionDetail.tsx"
check_file "$ROOT_PATH/src/components/questions/QuestionForm.tsx" "QuestionForm.tsx"

echo -e "\n📄 Файлы answers:"
check_file "$ROOT_PATH/src/components/answers/AnswerList.tsx" "AnswerList.tsx"
check_file "$ROOT_PATH/src/components/answers/AnswerForm.tsx" "AnswerForm.tsx"

echo -e "\n📄 Файлы pages:"
check_file "$ROOT_PATH/src/pages/HomePage.tsx" "HomePage.tsx"
check_file "$ROOT_PATH/src/pages/QuestionsPage.tsx" "QuestionsPage.tsx"
check_file "$ROOT_PATH/src/pages/QuestionPage.tsx" "QuestionPage.tsx"
check_file "$ROOT_PATH/src/pages/AskPage.tsx" "AskPage.tsx"
check_file "$ROOT_PATH/src/pages/LoginPage.tsx" "LoginPage.tsx"
check_file "$ROOT_PATH/src/pages/RegisterPage.tsx" "RegisterPage.tsx"
check_file "$ROOT_PATH/src/pages/UsersPage.tsx" "UsersPage.tsx"
check_file "$ROOT_PATH/src/pages/ProfilePage.tsx" "ProfilePage.tsx"

echo -e "\n📄 Файлы styles:"
check_file "$ROOT_PATH/src/styles/theme.ts" "theme.ts"

echo -e "\n========================================"
echo "✅ ПРОВЕРКА ЗАВЕРШЕНА"
echo "========================================"