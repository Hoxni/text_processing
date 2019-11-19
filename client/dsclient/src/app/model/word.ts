export class Word {
  word: string;
  frequency: bigint;
  tags: string[];
  lemma: string;
  lemmaTags: string[];

  constructor({word, frequency, tags, lemma, lemmaTags}){
    this.word = word;
    this.frequency = frequency;
    this.tags = tags;
    this.lemma = lemma;
    this.lemmaTags= lemmaTags;
  }

}
