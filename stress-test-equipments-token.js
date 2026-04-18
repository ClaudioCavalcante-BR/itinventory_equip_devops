import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 100 },
        { duration: '90s', target: 200 },
        { duration: '60s', target: 250 },
        { duration: '30s', target: 0 },
    ],
};

const BASE_URL = 'http://127.0.0.1:51725';
const TOKEN = __ENV.TOKEN;

const params = {
    headers: {
        Authorization: `Bearer ${TOKEN}`,
        'Content-Type': 'application/json',
    },
};

export default function () {
    for (let i = 0; i < 5; i++) {
        const res = http.get(`${BASE_URL}/api/equipments`, params);
        check(res, { 'status 200': (r) => r.status === 200 });
    }

    sleep(0.1);
}

// rodar token novo
//curl -X POST http://127.0.0.1:50681/api/usuarios/login \
//    -H "Content-Type: application/json" \
//    -d '{"email":"paulo.martins051@empresa.com","password":"123456"}'

// rode no terminal
// TOKEN='SEU_TOKEN_NOVO' k6 run stress-test-equipments-token.js