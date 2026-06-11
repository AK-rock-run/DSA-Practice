#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
struct student {
    int roll;
    char name[50], mobile[15], email[50];
    struct student *next;
};
int isDuplicate(struct student *head, int roll, char mobile[], char email[]) {
    while (head) {
        if (head->roll == roll || strcmp(head->mobile, mobile) == 0 || strcmp(head->email, email) == 0)
            return 1;
        head = head->next;
    }
    return 0;
}
int validateEmail(char email[]) {
    int len = strlen(email);
    if (len < 6) return 0;
    for (int i = 0; i < len; i++) email[i] = tolower(email[i]);
    char *at = strchr(email, '@');
    if (!at || (strcmp(at, "@vit.edu") && strcmp(at, "@gmail.com"))) return 0;
    int localLen = at - email;
    if (localLen < 3) return 0;
    if (!isalnum(email[0]) || !isalnum(email[localLen - 1])) return 0;
    for (int i = 0; i < localLen - 1; i++)
        if ((email[i] == '.' || email[i] == '_' || email[i] == '-') &&
            (email[i+1] == '.' || email[i+1] == '_' || email[i+1] == '-'))
            return 0;
    for (int i = 0; i < localLen; i++)
        if (!(islower(email[i]) || isdigit(email[i]) || email[i]=='.' || email[i]=='_' || email[i]=='-'))
            return 0;
    return 1;
}
struct student* createStudent() {
    struct student *newS = malloc(sizeof(struct student));
    char input[100];
    do {
        printf("Enter Roll Number: ");
        scanf("%s", input);
        int ok = 1; for(int i=0; input[i]; i++) if(!isdigit(input[i])) ok=0;
        newS->roll = ok ? atoi(input) : -1;
    } while (newS->roll <= 0);
    do {
        printf("Enter Name: ");
        scanf(" %[^\n]", newS->name);
    } while (strlen(newS->name) < 3);
    do {
        printf("Enter Mobile (10 digits): ");
        scanf("%s", newS->mobile);
    } while(strlen(newS->mobile)!=10 || strspn(newS->mobile,"0123456789")!=10);
    do {
        printf("Enter Email (@vit.edu or @gmail.com): ");
        scanf("%s", newS->email);
    } while (!validateEmail(newS->email));
    newS->next = NULL;
    return newS;
}
void addStudent(struct student **head, int afterRoll, int beforeRoll) {
    struct student *newS = createStudent();
    if (isDuplicate(*head, newS->roll, newS->mobile, newS->email)) {
        printf("Duplicate entry! Not added.\n");
        free(newS);
        return; }
    if (*head == NULL)  *head = newS;
    else if (afterRoll == -1 && beforeRoll == -1) { // Normal add at end
        struct student *temp = *head;
        while (temp->next) temp = temp->next;
        temp->next = newS;}
    else if (afterRoll != -1) { // Insert after roll
        struct student *temp = *head;
        while (temp && temp->roll != afterRoll) temp = temp->next;
        if (!temp) {
            printf("Roll not found. Added at end.\n");
            struct student *t=*head; while(t->next) t=t->next; t->next=newS;
        } else {
            newS->next = temp->next;
            temp->next = newS;
        }
    }
    else if (beforeRoll != -1) { // Insert before roll
        if ((*head)->roll == beforeRoll) {
            newS->next = *head;
            *head = newS;
        } else {
            struct student *temp = *head; 
            while (temp->next && temp->next->roll != beforeRoll)
                temp = temp->next;
            if (!temp->next) {
                printf("Roll not found. Added at end.\n");
                temp->next = newS;
            } else {
                newS->next = temp->next;
                temp->next = newS; } } }
    printf("Student added successfully!\n");
}
void deleteStudent(struct student **head) {
    if (!*head) {printf("List empty.\n"); return; }
    int roll; printf("Enter roll to delete: "); scanf("%d",&roll);
    struct student *temp = *head, *prev = NULL;
    while (temp && temp->roll != roll) {
        prev = temp;
        temp = temp->next;
    }
    if (!temp) {
        printf("Roll not found.\n");
        return;}
    if (!prev) *head = temp->next;
    else prev->next = temp->next;
    free(temp);
    printf("Student deleted!\n");
}
void display(struct student *head) {
    if (!head) { printf("No data.\n"); return; }
    int i=1;
    while (head) {
        printf("%d) Roll:%d  Name:%s  Mobile:%s  Email:%s\n",i++,head->roll,head->name,head->mobile,head->email);
        head = head->next;
    }
}
void search(struct student *head) {
    int roll; printf("Enter roll to search: "); scanf("%d",&roll);
    while (head) {
        if (head->roll == roll) {
            printf("Found: %s (%s, %s)\n",head->name,head->mobile,head->email);
            return;
        }
        head = head->next;
    }
    printf("Not found.\n");
}
void sort(struct student **head, int choice) {
    if (!*head || !(*head)->next) return;
    for (struct student *i=*head; i; i=i->next) {
        for (struct student *j=i->next; j; j=j->next) {
            int swap=0;
            if (choice==1 && i->roll > j->roll) swap=1;
            if (choice==2 && strcmp(i->name,j->name)>0) swap=1;
            if (swap) {
                struct student t=*i; *i=*j; *j=t;
                struct student *tmp=i->next; i->next=j->next; j->next=tmp;
            }
        }
    }
    printf("Sorted!\n");
}
int main() {
    struct student *head = NULL;
    int ch;
    do {
        printf("\n--- Menu ---\n1.Add\n2.Delete\n3.Display\n4.Search\n5.Sort\n6.Insert After Roll\n7.Insert Before Roll\n8.Exit\nChoice: ");
        scanf("%d",&ch);
        if(ch==1) addStudent(&head,-1,-1);
        else if(ch==2) deleteStudent(&head);
        else if(ch==3) display(head);
        else if(ch==4) search(head);
        else if(ch==5){int c;printf("Sort by 1.Roll 2.Name: ");scanf("%d",&c);sort(&head,c);}
        else if(ch==6){int r;printf("Insert after roll: ");scanf("%d",&r);addStudent(&head,r,-1);}
        else if(ch==7){int r;printf("Insert before roll: ");scanf("%d",&r);addStudent(&head,-1,r);}
    } while(ch!=8);
    while (head) {
        struct student *t=head; head=head->next; free(t);
    }
    return 0;
}
