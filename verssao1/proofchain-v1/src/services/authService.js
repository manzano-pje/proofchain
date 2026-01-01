import api from './api'

export default {
    async login(credentials) {
        // Example login call
        // const response = await api.post('/auth/login', credentials)
        // return response.data

        // Mock login for now
        return {
            token: 'mock-token-' + Date.now(),
            user: {
                name: 'Admin User',
                email: credentials.email,
                role: 'ADMIN'
            }
        }
    },

    logout() {
        // Perform any server-side logout if needed
        localStorage.removeItem('token')
    }
}
