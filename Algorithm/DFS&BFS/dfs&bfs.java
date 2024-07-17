import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static Queue<Integer> q = new LinkedList<>();
    public static Map<Integer, LinkedList<Integer>> map;
    public static boolean[] visited;
    public static int cnt = 0;
    public static void dfs (int n) {
        if (!map.containsKey(n))
            return;
        for (int a: map.get(n)) {
            if (!visited[a]) {
                visited[a] = true;
                dfs(a);
                cnt++;
            }
        }
    }
    public static void bfs (int n) {
        // 큐에 초기 노드를 넣고 시작
        q.offer(n);
        visited[n] = true;

        // 스택 채우기
        while (!q.isEmpty()) {
            n = q.poll();

            if (map.get(n) == null)
                continue;
            for (int a : map.get(n)) {
                if (!visited[a]) {
                    q.offer(a);
                    visited[a] = true;
                    cnt++;
                }
            }
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int num = Integer.parseInt(br.readLine());
        int pair = Integer.parseInt(br.readLine());

        visited = new boolean[num+1];
        map = new HashMap<>();
        for (int i = 0; i < pair; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            if (!map.containsKey(a))
                map.put(a, new LinkedList<>());
            map.get(a).add(b);
            if (!map.containsKey(b))
                map.put(b, new LinkedList<>());
            map.get(b).add(a);
        }

        visited[1] = true;
//        dfs(1);
        bfs(1);
        System.out.println(cnt);
    }
}