#include<stdio.h>
#include<stdlib.h>
#include<time.h>

#define ARRAY_SIZE 1000
#define WINDOW_SIZE 10

int main()
{
    int arr[ARRAY_SIZE];
    int i;
    srand(time(NULL));

    for(i=0;i<ARRAY_SIZE;i++){
        arr[i]=(rand()%100)+1;
    }
int max_sum=0;
int current_sum=0;
int max_index=0;

for (i=1;i<WINDOW_SIZE;i++){
    current_sum+=arr[i];
}
max_sum=current_sum;
for(i=1;i<ARRAY_SIZE-WINDOW_SIZE;i++){
    current_sum=current_sum-arr[i-1]+arr[i+WINDOW_SIZE-1];
    if (current_sum>max_sum){
        max_sum=current_sum;
        max_index=i;
    
    }
}
printf("Max sum of %d consecutive numbers: %d\n",WINDOW_SIZE,max_sum);
printf("Starting index of this sequence  : %d\n",max_index);
printf("Sequence :");
for (i=max_index;i<max_index+WINDOW_SIZE;i++){
    printf(" %d",arr[i]);
}
printf("\n");
return 0;
}
