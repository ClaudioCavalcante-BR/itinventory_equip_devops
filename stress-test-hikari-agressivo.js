import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 100 },
        { duration: '60s', target: 200 },
        { duration: '90s', target: 300 },
        { duration: '60s', target: 400 },
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        http_req_failed: ['rate<0.15'],
        http_req_duration: ['p(95)<6000'],
    },
};

const BASE_URL = 'http://127.0.0.1:8081';
const TOKEN = __ENV.TOKEN;

const params = {
    headers: {
        Authorization: `Bearer ${TOKEN}`,
        'Content-Type': 'application/json',
    },
};

export default function () {
    const requests = [];
    for (let i = 0; i < 12; i++) {
        requests.push(['GET', `${BASE_URL}/api/equipments`, null, params]);
    }

    const responses = http.batch(requests);

    for (const res of responses) {
        check(res, {
            'status 200': (r) => r.status === 200,
        });
    }

    sleep(0.02);
}