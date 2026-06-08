#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
struct student {
    int roll;
    char name[50], mobile[15], email[50];
};
int isDuplicate(struct student *s, int total, int roll, char mobile[], char email[]) {
    for (int i = 0; i < total; i++)
        if (s[i].roll == roll || strcmp(s[i].mobile, mobile) == 0 || strcmp(s[i].email, email) == 0)
            return 1;
    return 0;
}
int validateEmail(char email[]) {
    int len = strlen(email);
    if (len < 6) return 0;
    for (int i = 0; i < len; i++) email[i]=tolower(email[i]);
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
void addStudent(struct student **s, int *total, int afterRoll) {
    struct student newS;
    char input[100];
    do {
        printf("Enter Roll Number: ");
        scanf("%s", input);
        int ok = 1; for(int i=0; input[i]; i++) if(!isdigit(input[i])) ok=0;
        newS.roll = ok ? atoi(input) : -1;
    } while (newS.roll <= 0);
    do {
        printf("Enter Name: ");
        scanf(" %[^\n]", newS.name);
    } while (strlen(newS.name) < 3);
    do {
        printf("Enter Mobile (10 digits): "); 
        scanf("%s", newS.mobile);
    } while(strlen(newS.mobile)!=10 || strspn(newS.mobile,"0123456789")!=10);
    do {
        printf("Enter Email (@vit.edu or @gmail.com): ");
        scanf("%s", newS.email);
    } while (!validateEmail(newS.email));
    if (isDuplicate(*s, *total, newS.roll, newS.mobile, newS.email)) {
        printf("Duplicate entry! Not added.\n"); return;
    }
    *s = realloc(*s, (*total+1)*sizeof(struct student));
    if (afterRoll == -1) { // normal add
        (*s)[*total] = newS;
    } else { // insert after roll
        int pos = *total; 
        for(int i=0;i<*total;i++) if((*s)[i].roll==afterRoll) { pos=i+1; break; }
        for(int i=*total;i>pos;i--) (*s)[i]=(*s)[i-1];
        (*s)[pos]=newS;
    }
    (*total)++;
    printf("Student added successfully!\n");
}
void deleteStudent(struct student **s, int *total) {
    int roll; printf("Enter roll to delete: "); scanf("%d",&roll);
    for (int i=0;i<*total;i++) if((*s)[i].roll==roll) {
        for(int j=i;j<*total-1;j++) (*s)[j]=(*s)[j+1];
        (*s)=realloc(*s, (--*total)*sizeof(struct student));
        printf("Student deleted!\n"); return;
    }
    printf("Roll not found.\n");
}
void display(struct student *s, int total) {
    if (!total) return printf("No data.\n"), (void)0;
    for(int i=0;i<total;i++)
        printf("%d) Roll:%d  Name:%s  Mobile:%s  Email:%s\n",i+1,s[i].roll,s[i].name,s[i].mobile,s[i].email);
}
void search(struct student *s, int total) {
    int roll; printf("Enter roll to search: "); scanf("%d",&roll);
    for(int i=0;i<total;i++) if(s[i].roll==roll) {
        printf("Found: %s (%s, %s)\n",s[i].name,s[i].mobile,s[i].email); return;
    }
    printf("Not found.\n");
}
void sort(struct student *s,int total) {
    int c; printf("Sort by 1.Roll 2.Name: "); scanf("%d",&c);
    for(int i=0;i<total-1;i++) for(int j=i+1;j<total;j++) {
        int swap=0;
        if(c==1 && s[i].roll>s[j].roll) swap=1;
        if(c==2 && strcmp(s[i].name,s[j].name)>0) swap=1;
        if(swap){struct student t=s[i];s[i]=s[j];s[j]=t;}
    }
    printf("Sorted!\n");
}
int main() {
    struct student *s=NULL; int total=0,ch;
    do {
        printf("\n--- Menu ---\n1.Add\n2.Delete\n3.Display\n4.Search\n5.Sort\n6.Insert After Roll\n9.Insert before Roll\n8.Exit\nChoice: ");
        scanf("%d",&ch);
        if(ch==1) addStudent(&s,&total,-1);
        else if(ch==2) deleteStudent(&s,&total);
        else if(ch==3) display(s,total);
        else if(ch==4) search(s,total);
        else if(ch==5) sort(s,total);
        else if(ch==6){int r;printf("Insert after roll: ");scanf("%d",&r);addStudent(&s,&total,r);}
        else if (ch==7){int r;printf("Insert before roll: ");scanf("%d",&r);addStudent(&s,&total,r);}
    } while(ch!=8);
    free(s); return 0;
}
