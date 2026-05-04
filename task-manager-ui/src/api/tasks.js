import axios from "axios"

const API = axios.create({
    baseURL: "http://localhost:8080",
});

export const getTasks = (page = 0, size = 10) =>
    API.get(`/tasks?page=${page}&size=${size}`);

export const deleteTask = (id) => API.delete(`/tasks/${id}`)