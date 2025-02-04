BEGIN {
	N = 100
	printf("#SinglyLinkedList_0\n")
	for (i = 0; i < N - 1; i++) {
		printf("SinglyLinkedList/SinglyLinkedList_%d/next/SinglyLinkedList_%d\n", i, i + 1)
		printf("SinglyLinkedList/SinglyLinkedList_%d/next/null\n", i)
	}
	printf("SinglyLinkedList/SinglyLinkedList_%d/next/null\n", N - 1)
}
