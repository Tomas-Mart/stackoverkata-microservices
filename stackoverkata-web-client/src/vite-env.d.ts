/// <reference types="vite/client" />

interface ImportMetaEnv {
    readonly VITE_API_BASE_URL: string;
    readonly VITE_AUTH_SERVICE_URL: string;
    readonly VITE_PROFILE_SERVICE_URL: string;
    readonly VITE_RESOURCE_SERVICE_URL: string;
    readonly VITE_APP_NAME: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}