import csv
from nltk.translate.bleu_score import sentence_bleu, SmoothingFunction, corpus_bleu
from rouge_score import rouge_scorer
import numpy as np

def calculate_bleu(reference_sentences, generated_sentence, n):
    # 使用平滑函数来避免BLEU得分为0
    smoothing = SmoothingFunction().method1
    bleu_score = sentence_bleu(reference_sentences, generated_sentence, weights=[1/n]*n + [0]*(4-n), smoothing_function=smoothing)
    return bleu_score

def calculate_rouge(reference_text, generated_text):
    scorer = rouge_scorer.RougeScorer(['rouge1', 'rougeL'], use_stemmer=True)
    scores = scorer.score(reference_text, generated_text)
    return scores

def main(csv_file_path):
    bleu1_scores = []
    bleu4_scores = []
    rouge1_scores = []
    rougeL_scores = []

    with open(csv_file_path, mode='r', encoding='utf-8-sig') as file:
        reader = csv.DictReader(file)
        
        for row in reader:
            reference_text = row['removed-text']
            generated_text = row['inserted-text']
            
            # Tokenize the sentences for BLEU calculation
            reference_tokens = [reference_text.split()]
            generated_tokens = generated_text.split()
            
            # Calculate BLEU-1 score
            bleu1_score = calculate_bleu(reference_tokens, generated_tokens, 1)
            bleu1_scores.append(bleu1_score)
            print(f'BLEU-1 score: {bleu1_score:.3f}')
            
            # Calculate BLEU-4 score
            bleu4_score = calculate_bleu(reference_tokens, generated_tokens, 4)
            bleu4_scores.append(bleu4_score)
            print(f'BLEU-4 score: {bleu4_score:.3f}')
            
            # Calculate ROUGE score
            rouge_scores = calculate_rouge(reference_text, generated_text)
            rouge1_scores.append(rouge_scores['rouge1'].fmeasure)
            rougeL_scores.append(rouge_scores['rougeL'].fmeasure)
            
            print(f'ROUGE-1 score: {rouge_scores["rouge1"].fmeasure:.3f}')
            print(f'ROUGE-L score: {rouge_scores["rougeL"].fmeasure:.3f}')
            print('---')

    # Calculate and print average scores
    avg_bleu1 = np.mean(bleu1_scores) if bleu1_scores else 0
    avg_bleu4 = np.mean(bleu4_scores) if bleu4_scores else 0
    avg_rouge1 = np.mean(rouge1_scores) if rouge1_scores else 0
    avg_rougeL = np.mean(rougeL_scores) if rougeL_scores else 0

    print(f'\nAverage BLEU-1 score: {avg_bleu1:.3f}')
    print(f'Average BLEU-4 score: {avg_bleu4:.3f}')
    print(f'Average ROUGE-1 score: {avg_rouge1:.3f}')
    print(f'Average ROUGE-L score: {avg_rougeL:.3f}')

if __name__ == "__main__":
    csv_file_path = 'zookeeper_rq1dd.csv'  # 这里替换成你的CSV文件路径
    main(csv_file_path)
