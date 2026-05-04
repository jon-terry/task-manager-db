import { useEffect, useState } from "react";
import { getTasks, deleteTask } from "../api/tasks";

export default function TaskList() {
    const [tasks, setTasks] = useState([]);
    const [page, setPage] = useState(0);

    const loadTasks = async () => {
        try {
            const res = await getTasks(page, 10);
            setTasks(res.data.content);
        } catch (err) {
            console.error("Failed to load tasks:", err);
        }
    };

    useEffect(() => {
        loadTasks();
    }, [page]);

    const handleDelete = async (id) => {
        await deleteTask(id);
        loadTasks();
    };

    return (
        <div>
            <h2>Tasks</h2>

            <ul>
                {tasks.map((t) => (
                    <li key={t.id}>
                        {t.title} — {t.completed ? "Done" : "Pending"}
                        <button onClick={() => handleDelete(t.id)}>Delete</button>
                    </li>
                ))}
            </ul>

            <button onClick={() => setPage(page - 1)} disabled={page === 0}>
                Prev
            </button>
            <button onClick={() => setPage(page + 1)}>
                Next
            </button>
        </div>
    );
}
