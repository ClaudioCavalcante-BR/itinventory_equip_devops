import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 50 },
        { duration: '60s', target: 100 },
        { duration: '60s', target: 150 },
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        http_req_failed: ['rate<0.05'],
        http_req_duration: ['p(95)<2000'],
    },
};

const BASE_URL = 'http://127.0.0.1:61267';
const TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzc1MzUwNTUwLCJleHAiOjE3NzUzNTQxNTAsIm5hbWUiOiJQYXVsbyBIZW5yaXF1ZSBNYXJ0aW5zIiwiZW1haWwiOiJwYXVsby5tYXJ0aW5zMDUxQGVtcHJlc2EuY29tIiwiam9iVGl0bGUiOiJBZG1pbmlzdHJhZG9yIGRvIFNpc3RlbWEiLCJwcm9maWxlQ29kZSI6IkFETUlOIiwibml2ZWxBY2Vzc28iOjN9.uOS8t-2Ef5RS6eXL7bA4HQt_dNwaEnhEuBZB_ix8AiI';

const params = {
    headers: {
        Authorization: `Bearer ${TOKEN}`,
        'Content-Type': 'application/json',
    },
};

export default function () {
    const r1 = http.get(`${BASE_URL}/api/equipments`, params);
    check(r1, { 'GET /api/equipments status 200': (r) => r.status === 200 });

    const r2 = http.get(`${BASE_URL}/api/equipments`, params);
    check(r2, { 'GET /api/equipments 2 status 200': (r) => r.status === 200 });

    const r3 = http.get(`${BASE_URL}/api/equipments`, params);
    check(r3, { 'GET /api/equipments 3 status 200': (r) => r.status === 200 });

    sleep(0.2);
}
// comando para rodar no terminal: k6 run stress-test-equipments.js