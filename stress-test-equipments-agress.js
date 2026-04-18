import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 100 },
        { duration: '90s', target: 200 },
        { duration: '60s', target: 250 },
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        http_req_failed: ['rate<0.10'],
        http_req_duration: ['p(95)<4000'],
    },
};

const BASE_URL = 'http://127.0.0.1:61267';
const TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzc1NDMwMDEzLCJleHAiOjE3NzU0MzM2MTMsIm5hbWUiOiJQYXVsbyBIZW5yaXF1ZSBNYXJ0aW5zIiwiZW1haWwiOiJwYXVsby5tYXJ0aW5zMDUxQGVtcHJlc2EuY29tIiwiam9iVGl0bGUiOiJBZG1pbmlzdHJhZG9yIGRvIFNpc3RlbWEiLCJwcm9maWxlQ29kZSI6IkFETUlOIiwibml2ZWxBY2Vzc28iOjN9.8GOGhnEUrUlO2UBhhS8QGtacifO4-hxbPZ2iv7qoDHE';

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